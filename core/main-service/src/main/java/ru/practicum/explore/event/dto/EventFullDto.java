package ru.practicum.explore.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AccessLevel;
import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.explore.category.dto.CategoryDto;
import ru.practicum.explore.event.enums.State;
import ru.practicum.explore.event.model.Location;
import ru.practicum.explore.user.dto.UserShortDto;

import java.time.LocalDateTime;

import static ru.practicum.explore.formatter.DateTimeFormat.TIME_PATTERN;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class EventFullDto {
    Long id;
    String annotation;
    CategoryDto category;
    Integer confirmedRequests;
    @JsonFormat(pattern = TIME_PATTERN)
    LocalDateTime createdOn;
    LocalDateTime publishedOn;
    String description;
    @JsonFormat(pattern = TIME_PATTERN)
    LocalDateTime eventDate;
    UserShortDto initiator;
    Location location;
    Boolean paid;
    Integer participantLimit;
    State state;
    Boolean requestModeration;
    String title;
    Long views;
    Boolean commenting;
}