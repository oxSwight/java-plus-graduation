package ru.practicum.interaction.api.dto.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.interaction.api.enums.event.StateAction;

import java.time.LocalDateTime;

import static ru.practicum.interaction.api.utils.date.DateTimeFormat.TIME_PATTERN;

@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class UpdateEventUserRequest {
    @Size(min = 20, max = 2000)
    String annotation;
    Long category;
    @Size(min = 20, max = 7000)
    String description;
    @JsonFormat(pattern = TIME_PATTERN)
    @Future
    LocalDateTime eventDate;
    Location location;
    Boolean paid;
    @PositiveOrZero
    Integer participantLimit;
    Boolean requestModeration;
    @Size(min = 3, max = 120)
    String title;
    StateAction stateAction;
    Boolean commenting;
}
