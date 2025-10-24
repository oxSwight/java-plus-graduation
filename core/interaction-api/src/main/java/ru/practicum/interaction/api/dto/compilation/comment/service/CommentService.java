package ru.practicum.interaction.api.dto.compilation.comment.service;

import ru.practicum.interaction.api.dto.compilation.comment.dto.CommentDto;
import ru.practicum.interaction.api.dto.compilation.comment.dto.NewCommentDto;
import ru.practicum.interaction.api.dto.compilation.comment.enums.SortType;
import ru.practicum.ewm.user.dto.UserDtoForAdmin;

import java.util.List;

public interface CommentService {
    CommentDto createComment(Long eventId, Long userId, NewCommentDto newCommentDto);

    CommentDto updateComment(Long userId, Long eventId, Long commentId, NewCommentDto newCommentDto);

    void deleteComment(Long userId, Long eventId, Long commentId);

    void deleteComment(Long commentId, Long eventId);

    List<CommentDto> getAllComments(Long eventId, SortType sortType, Integer from, Integer size);

    CommentDto addLike(Long userId, Long commentId);

    UserDtoForAdmin addBanCommited(Long userId, Long eventId);

    void deleteBanCommited(Long userId, Long eventId);

    void deleteLike(Long userId, Long commentId);

    CommentDto getComment(Long id);
}
