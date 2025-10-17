package ru.practicum.explore.comment.dto;

import lombok.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

import static ru.practicum.explore.formatter.DateTimeFormat.TIME_PATTERN;

@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class CommentDto {
    Long id;
    String text;
    Long eventId;
    String eventName;
    String authorName;
    Integer likes;
    @JsonFormat(pattern = TIME_PATTERN)
    LocalDateTime created;
}