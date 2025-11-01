package ru.practicum.comment.service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.comment.service.model.BanComment;

public interface BanCommentRepository extends JpaRepository<BanComment, Long> {

    BanComment findByUserIdAndEventId(Long userId, Long eventId);
}