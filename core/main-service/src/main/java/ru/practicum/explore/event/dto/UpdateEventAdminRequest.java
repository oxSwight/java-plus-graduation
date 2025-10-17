package ru.practicum.explore.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Embedded;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.explore.event.enums.StateAction;
import ru.practicum.explore.event.model.Location;
import ru.practicum.explore.event.service.UpdateObject;

import java.time.LocalDateTime;

import static ru.practicum.explore.formatter.DateTimeFormat.TIME_PATTERN;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateEventAdminRequest {

    @Size(min = 20, max = 2000, message = "Для описания требуется от 20 до 2000 символов.", groups = UpdateObject.class)
    String annotation;

    Long category;

    @Size(min = 20, max = 7000, message = "Для описания требуется от 20 до 7000 символов.", groups = UpdateObject.class)
    String description;

    @JsonFormat(pattern = TIME_PATTERN)
    LocalDateTime eventDate;

    @Embedded
    Location location;

    Boolean paid;

    @PositiveOrZero
    Integer participantLimit;

    Boolean requestModeration;

    StateAction stateAction;

    @Size(min = 3, max = 120, message = "Для заголовка требуется от 3 до 120 символов.", groups = UpdateObject.class)
    String title;

    Boolean commenting;
}
