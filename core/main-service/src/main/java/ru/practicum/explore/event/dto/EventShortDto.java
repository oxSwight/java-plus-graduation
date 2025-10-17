package ru.practicum.explore.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.explore.category.dto.CategoryDto;
import ru.practicum.explore.user.dto.UserShortDto;

import java.time.LocalDateTime;

import static ru.practicum.explore.formatter.DateTimeFormat.TIME_PATTERN;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class EventShortDto {
    Long id;
    String annotation;
    CategoryDto category;
    Integer confirmedRequests;
    @JsonFormat(pattern = TIME_PATTERN)
    LocalDateTime eventDate;
    LocalDateTime publishedOn;
    UserShortDto initiator;
    Boolean paid;
    String title;
    Long views;
    Boolean commenting;
}