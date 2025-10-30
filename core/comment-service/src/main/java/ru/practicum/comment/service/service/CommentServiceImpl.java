package ru.practicum.comment.service.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.comment.service.mapper.CommentMapper;
import ru.practicum.comment.service.model.Comment;
import ru.practicum.comment.service.repository.BanCommentRepository;
import ru.practicum.comment.service.repository.CommentRepository;
import ru.practicum.interaction.api.dto.comment.CommentDto;
import ru.practicum.interaction.api.dto.comment.NewCommentDto;
import ru.practicum.interaction.api.dto.event.EventFullDto;
import ru.practicum.interaction.api.dto.user.UserDto;
import ru.practicum.interaction.api.dto.user.UserDtoForAdmin;
import ru.practicum.interaction.api.enums.comment.SortType;
import ru.practicum.interaction.api.exception.NotFoundException;
import ru.practicum.interaction.api.exception.ValidationException;
import ru.practicum.interaction.api.feignClient.client.event.AdminEventClient;
import ru.practicum.interaction.api.feignClient.client.user.UserClient;
import ru.practicum.interaction.api.enums.event.State;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final UserClient userClient;
    private final AdminEventClient adminEventClient;
    private final BanCommentRepository banCommentRepository;

    private final String USER_NOT_FOUND = "Пользователь не найден.";
    private final String INCORRECTLY_EVENT_ID = "Некорректно указан eventId.";
    private final String EVENT_NOT_FOUND = "Событие не найдено.";
    private final String USER_NOT_COMMENT = "Пользователь не оставлял комментарий.";

    @Override
    public CommentDto createComment(Long eventId, Long userId, NewCommentDto newCommentDto) {
        checkEventId(eventId);
        EventFullDto event = getEvent(eventId);
        if (event.getState() != State.PUBLISHED) {
            throw new ValidationException("Нельзя комментировать неопубликованное событие");
        }
        UserDto user = getUser(userId);
        if (userClient.findById(userId)== null) {
            throw new ValidationException("Для данного пользователя стоит запрет на комментирование данного события");
        }
        if (!event.getCommenting()) {
            throw new ValidationException("Данное событие нельзя комментировать");
        }
        Comment comment = CommentMapper.toComment(newCommentDto, eventId, userId);
        return CommentMapper.toCommentDto(commentRepository.save(comment),event.getAnnotation(),user.getName());
    }

    @Override
    public CommentDto updateComment(Long userId, Long eventId, Long commentId, NewCommentDto newCommentDto) {
        checkEventId(eventId);
        UserDto user = getUser(userId);
        EventFullDto event = getEvent(eventId);
        if (userClient.findById(userId)==null) {
            throw new NotFoundException(USER_NOT_FOUND);
        }
        if (adminEventClient.findById(eventId)==null) {
            throw new NotFoundException(EVENT_NOT_FOUND);
        }
        Comment comment = checkComment(commentId);
        if (!Objects.equals(comment.getEventId(), eventId)) {
            throw new ValidationException(INCORRECTLY_EVENT_ID);
        }
        if (comment.getAuthorId().equals(userId)) {
            comment.setText(newCommentDto.getText());
        } else {
            throw new ValidationException(USER_NOT_COMMENT);
        }
        return CommentMapper.toCommentDto(comment,event.getAnnotation(), user.getName());
    }

    @Override
    public void deleteComment(Long userId, Long eventId, Long commentId) {
        checkEventId(eventId);
        if (userClient.findById(userId)== null) {
            throw new NotFoundException(USER_NOT_FOUND);
        }
        if (adminEventClient.findById(eventId)==null) {
            throw new NotFoundException(EVENT_NOT_FOUND);
        }
        Comment comment = checkComment(commentId);
        if (!Objects.equals(comment.getEventId(), eventId)) {
            throw new ValidationException(INCORRECTLY_EVENT_ID);
        }
        if (comment.getAuthorId().equals(userId)) {
            commentRepository.deleteById(commentId);
        } else {
            throw new ValidationException(USER_NOT_COMMENT);
        }
    }

    @Override
    public void deleteComment(Long commentId, Long eventId) {
        checkEventId(eventId);
        Comment comment = checkComment(commentId);
        if (!Objects.equals(comment.getEventId(), eventId)) {
            throw new ValidationException(INCORRECTLY_EVENT_ID);
        }
        commentRepository.deleteById(commentId);
    }

    @Override
    public List<CommentDto> getAllComments(Long eventId, SortType sortType, Integer from, Integer size) {
        Pageable pageable = PageRequest.of(from / size, size);
        List<CommentDto> comments = commentRepository.findAllByEventId(eventId, pageable)
                .stream()
                .map(CommentMapper::toCommentDto)
                .toList();
        if (sortType == SortType.LIKES) {
            return comments
                    .stream()
                    .sorted(Comparator.comparing(CommentDto::getLikes).reversed()).toList();
        } else {
            return comments.stream().sorted(Comparator.comparing(CommentDto::getCreated).reversed()).toList();
        }
    }

    @Override
    public CommentDto addLike(Long userId, Long commentId) {
        if (userClient.findById(userId)== null) {
            throw new NotFoundException(USER_NOT_FOUND);
        }
        Comment comment = checkComment(commentId);
        if (comment.getAuthorId().equals(userId)) {
            throw new ValidationException("Пользователь не может лайкать свой комментарий");
        }
        if (!comment.getLikes().add(userId)) {
            throw new ValidationException("Нельзя поставить лайк второй раз");
        }
        UserDto user = getUser(comment.getAuthorId());
        EventFullDto event = getEvent(comment.getEventId());
        return CommentMapper.toCommentDto(comment, event.getAnnotation(), user.getName());
    }

    @Override
    public void deleteLike(Long userId, Long commentId) {
        if (userClient.findById(userId)== null) {
            throw new NotFoundException(USER_NOT_FOUND);
        }
        Comment comment = checkComment(commentId);
        if (!comment.getLikes().remove(userId)) {
            throw new NotFoundException("Пользователь не лайкал комментарий с id: " + commentId);
        }
    }

    @Override
    public CommentDto getComment(Long id) {
        Comment comment = checkComment(id);
        UserDto user = getUser(comment.getAuthorId());
        EventFullDto event = getEvent(comment.getEventId());
        return CommentMapper.toCommentDto(comment, event.getAnnotation(), user.getName());
    }

    @Override
    public UserDtoForAdmin addBanCommited(Long userId, Long eventId) {
        checkEventId(eventId);
        if (checkExistForbiddenCommentEvents(userId, eventId)) {
            throw new ValidationException("Уже добавлен такой запрет на комментирование");
        }
        return userClient.adminFindById(userId);
    }

    @Override
    public void deleteBanCommited(Long userId, Long eventId) {
        checkEventId(eventId);
        if (!this.removeForbiddenCommentEvents(userId, eventId)) {
            throw new NotFoundException("Такого запрета на комментирование не найдено");
        }
   }

    private boolean removeForbiddenCommentEvents(Long userId, Long eventId) {
        var comment = banCommentRepository.findByUserIdAndEventId(userId, eventId);
        if (comment == null) {
            return false;
        }
        banCommentRepository.deleteById(comment.getId());
        return true;
    }

    private boolean checkExistForbiddenCommentEvents(Long userId, Long eventId) {
        var comment = banCommentRepository.findByUserIdAndEventId(userId, eventId);
        return comment != null;
    }

    private UserDto getUser(Long userId) {
        return userClient.findById(userId);
    }

    private EventFullDto getEvent(Long eventId) {
        return adminEventClient.findById(eventId);
    }

    private Comment checkComment(Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException("Комментарий не найден"));
    }

    private void checkEventId(Long eventId) {
        if (eventId == 0) {
            throw new ValidationException("Не задан eventId");
        }
    }
}
