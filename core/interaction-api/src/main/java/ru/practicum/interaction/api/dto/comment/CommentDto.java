package ru.practicum.interaction.api.dto.comment;

import lombok.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.experimental.FieldDefaults;
import ru.practicum.interaction.api.utils.date.DateTimeFormat;
import java.time.LocalDateTime;


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
    @JsonFormat(pattern = DateTimeFormat.TIME_PATTERN)
    LocalDateTime created;
}
