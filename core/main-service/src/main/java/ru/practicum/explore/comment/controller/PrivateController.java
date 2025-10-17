package ru.practicum.explore.comment.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore.comment.dto.CommentDto;
import ru.practicum.explore.comment.dto.NewCommentDto;
import ru.practicum.explore.comment.service.CommentService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users/{userId}/comments")
@Slf4j
public class PrivateController {

    private final CommentService commentService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CommentDto createComment(@PathVariable Long userId,
                                    @RequestParam(defaultValue = "0") Long eventId,
                                    @RequestBody NewCommentDto newCommentDto) {
        log.info("Создание комментария пользователем {} для события {}: {}", userId, eventId, newCommentDto);
        return commentService.createComment(eventId, userId, newCommentDto);
    }

    @PatchMapping("/{commentId}")
    public CommentDto updateComment(@PathVariable Long userId,
                                    @PathVariable Long commentId,
                                    @RequestParam(defaultValue = "0") Long eventId,
                                    @RequestBody NewCommentDto newCommentDto) {
        log.info("Редактирование комментария {} пользователем {}: {}", commentId, userId, newCommentDto);
        return commentService.updateComment(userId, eventId, commentId, newCommentDto);
    }

    @DeleteMapping("/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteComment(@PathVariable Long userId,
                              @PathVariable Long commentId,
                              @RequestParam(defaultValue = "0") Long eventId) {
        log.info("Удаление комментария {} пользователем {}", commentId, userId);
        commentService.deleteComment(userId, eventId, commentId);
    }

    @PutMapping("/{commentId}/like")
    public CommentDto addLike(@PathVariable Long userId,
                              @PathVariable Long commentId) {
        log.info("Пользователь {} добавил лайк к комментарию {}", userId, commentId);
        return commentService.addLike(userId, commentId);
    }

    @DeleteMapping("/{commentId}/like")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteLike(@PathVariable Long userId,
                           @PathVariable Long commentId) {
        log.info("Пользователь {} удалил лайк с комментария {}", userId, commentId);
        commentService.deleteLike(userId, commentId);
    }
}
