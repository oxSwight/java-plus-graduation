package ru.practicum.explore.event.controller;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore.event.dto.EventAdminFilter;
import ru.practicum.explore.event.dto.EventFullDto;
import ru.practicum.explore.event.dto.UpdateEventAdminRequest;
import ru.practicum.explore.event.enums.State;
import ru.practicum.explore.event.service.EventService;
import ru.practicum.explore.event.service.UpdateObject;
import ru.practicum.explore.exception.InvalidDateTimeException;

import java.time.LocalDateTime;
import java.util.List;

import static ru.practicum.explore.formatter.DateTimeFormat.TIME_PATTERN;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/events")
public class AdminEventController {

    private final EventService eventService;

    @GetMapping
    public List<EventFullDto> getEventsForAdmin(@RequestParam(required = false) List<Long> users,
                                                @RequestParam(required = false) List<State> states,
                                                @RequestParam(required = false) List<Long> categories,
                                                @RequestParam(required = false) @DateTimeFormat(pattern = TIME_PATTERN) LocalDateTime rangeStart,
                                                @RequestParam(required = false) @DateTimeFormat(pattern = TIME_PATTERN) LocalDateTime rangeEnd,
                                                @PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
                                                @Positive @RequestParam(defaultValue = "10") Integer size) {
        log.info("Получен запрос на получение событий для администратора.");

        if (rangeStart != null && rangeEnd != null && !rangeStart.isBefore(rangeEnd)) {
            throw new InvalidDateTimeException("Дата окончания события не может быть раньше даты начала события.");
        }

        EventAdminFilter filter = EventAdminFilter.builder()
                .users(users)
                .states(states)
                .categories(categories)
                .rangeStart(rangeStart)
                .rangeEnd(rangeEnd)
                .from(from)
                .size(size)
                .build();

        return eventService.getEventsForAdmin(filter);
    }

    @PatchMapping("/{eventId}")
    public EventFullDto updateEvent(@PositiveOrZero @PathVariable Long eventId,
                                    @Validated(UpdateObject.class) @RequestBody UpdateEventAdminRequest updateRequest) {
        log.info("Получен запрос на обновление события администратором с id={}", eventId);
        return eventService.updateEventAdmin(eventId, updateRequest);
    }
}
