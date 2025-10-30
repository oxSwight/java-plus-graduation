package ru.practicum.event.service.event.controller;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.event.service.event.service.EventService;
import ru.practicum.interaction.api.UpdateObject;
import ru.practicum.interaction.api.dto.event.EventAdminFilter;
import ru.practicum.interaction.api.dto.event.EventFullDto;
import ru.practicum.interaction.api.dto.event.UpdateEventAdminRequest;
import ru.practicum.interaction.api.enums.event.State;
import ru.practicum.interaction.api.exception.InvalidDateTimeException;

import java.time.LocalDateTime;
import java.util.List;

import static ru.practicum.interaction.api.utils.date.DateTimeFormat.TIME_PATTERN;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/admin/events")
@Validated
public class AdminEventController {

    private final EventService eventService;

    @GetMapping
    public List<EventFullDto>
    getEventsForAdmin(@RequestParam(required = false) List<Long> users,
                      @RequestParam(required = false) List<State> states,
                      @RequestParam(required = false) List<Long> categories,
                      @RequestParam(required = false) @DateTimeFormat(pattern = TIME_PATTERN) LocalDateTime rangeStart,
                      @RequestParam(required = false) @DateTimeFormat(pattern = TIME_PATTERN) LocalDateTime rangeEnd,
                      @PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
                      @Positive @RequestParam(defaultValue = "10") Integer size) {
        log.info("Получение полной информации обо всех событиях подходящих под переданные условия.");

        var filter = EventAdminFilter
                .builder()
                .users(users)
                .states(states)
                .categories(categories)
                .from(from)
                .size(size)
                .build();

        if (rangeStart != null && rangeEnd != null) {
            filter.setRangeStart(rangeStart);
            filter.setRangeEnd(rangeEnd);
            if (!filter.getRangeStart().isBefore(filter.getRangeEnd())) {
                throw new InvalidDateTimeException("Дата окончания события не может быть раньше даты начала события.");
            }
        }
        try {
            return eventService.getEventsForAdmin(filter);
        } catch (Exception e) {
            log.error("При запуске с параметрами " + filter, e);
            throw e;
        }
    }

    @PatchMapping("/{eventId}")
    public EventFullDto updateEvent(@PositiveOrZero @PathVariable Long eventId,
                                    @Validated(UpdateObject.class) @RequestBody UpdateEventAdminRequest updateEventAdminRequest) {
        log.info("Редактирование данных любого события администратором.");

        try {
            return eventService.updateEventAdmin(eventId, updateEventAdminRequest);
        } catch (Exception e) {
            log.error("При запуске updateEvent c eventId" + eventId + ", параметрами " + updateEventAdminRequest, e);
            throw e;
        }
    }

    @GetMapping("/{id}")
    public EventFullDto findById(@PathVariable("id") @Positive Long id) {
        log.info("Получение подробной информации об опубликованном событии по его идентификатору.");

        try {
            return eventService.getAdminEventById(id);
        } catch (Exception e) {
            log.error("При запуске с параметрами id " + id, e);
            throw e;
        }
    }

    @PutMapping("/request/{eventId}")
    public void setConfirmedRequests(@PathVariable("eventId") Long eventId, @RequestBody Integer count) {
        eventService.setConfirmedRequests(eventId, count);
    }
}
