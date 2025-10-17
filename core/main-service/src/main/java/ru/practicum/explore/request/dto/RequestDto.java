package ru.practicum.explore.request.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.explore.request.enums.Status;

import java.time.LocalDateTime;

import static ru.practicum.explore.formatter.DateTimeFormat.TIME_PATTERN;

@Getter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
public class RequestDto {

    Long id;
    @JsonFormat(pattern = TIME_PATTERN)
    LocalDateTime created;
    Long event;
    Long requester;
    Status status;
}