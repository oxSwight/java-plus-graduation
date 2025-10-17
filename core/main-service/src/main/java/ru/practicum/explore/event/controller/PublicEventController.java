package ru.practicum.explore.event.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore.event.dto.EventFullDto;
import ru.practicum.explore.event.dto.EventPublicFilter;
import ru.practicum.explore.event.dto.EventShortDto;
import ru.practicum.explore.event.enums.SortType;
import ru.practicum.explore.event.service.EventService;
import ru.practicum.explore.exception.InvalidDateTimeException;

import java.time.LocalDateTime;
import java.util.List;

import static ru.practicum.explore.formatter.DateTimeFormat.TIME_PATTERN;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/events")
public class PublicEventController {

    private final EventService eventService;

    @GetMapping
    public List<EventShortDto> getEventsByFilter(HttpServletRequest request,
                                                 @RequestParam(defaultValue = "") String text,
                                                 @RequestParam(required = false) List<Long> categories,
                                                 @RequestParam(required = false) Boolean paid,
                                                 @RequestParam(required = false) @DateTimeFormat(pattern = TIME_PATTERN) LocalDateTime rangeStart,
                                                 @RequestParam(required = false) @DateTimeFormat(pattern = TIME_PATTERN) LocalDateTime rangeEnd,
                                                 @RequestParam(defaultValue = "false") Boolean onlyAvailable,
                                                 @RequestParam(defaultValue = "EVENT_DATE") SortType sort,
                                                 @PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
                                                 @Positive @RequestParam(defaultValue = "10") Integer size) {

        log.info("Запрос на получение событий: text={}, categories={}, paid={}, rangeStart={}, rangeEnd={}, onlyAvailable={}, sort={}, from={}, size={}",
                text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort, from, size);

        if (rangeStart != null && rangeEnd != null && !rangeStart.isBefore(rangeEnd)) {
            throw new InvalidDateTimeException("Дата окончания события не может быть раньше даты начала события.");
        }

        EventPublicFilter filter = EventPublicFilter.builder()
                .text(text)
                .categories(categories)
                .paid(paid)
                .rangeStart(rangeStart)
                .rangeEnd(rangeEnd)
                .onlyAvailable(onlyAvailable)
                .sort(sort)
                .from(from)
                .size(size)
                .build();

        return eventService.getPublicEventsByFilter(request, filter);
    }

    @GetMapping("/{id}")
    public EventFullDto getEventById(HttpServletRequest request, @PathVariable @Positive Long id) {
        log.info("Получен запрос на получение события с id={}", id);
        return eventService.getPublicEventById(request, id);
    }
}
