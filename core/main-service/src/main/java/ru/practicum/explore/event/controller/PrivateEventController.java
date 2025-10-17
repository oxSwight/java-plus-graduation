package ru.practicum.explore.event.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore.event.dto.*;
import ru.practicum.explore.event.service.EventService;
import ru.practicum.explore.request.dto.RequestDto;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/users/{userId}/events")
public class PrivateEventController {

    private final EventService eventService;

    @GetMapping
    public List<EventShortDto> getEventsOfUser(@PathVariable Long userId,
                                               @RequestParam(defaultValue = "0") Integer from,
                                               @RequestParam(defaultValue = "10") Integer size) {
        log.info("Получен запрос на получение событий пользователя с id={}", userId);
        return eventService.getEventsOfUser(userId, from, size);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EventFullDto addEvent(@PathVariable Long userId,
                                 @Valid @RequestBody NewEventDto eventDto) {
        log.info("Получен запрос на создание нового события пользователем с id={}", userId);
        return eventService.addEvent(eventDto, userId);
    }

    @GetMapping("/{eventId}")
    public EventFullDto getEventOfUser(@PathVariable Long userId,
                                       @PathVariable Long eventId) {
        log.info("Получен запрос на получение события {} пользователем {}", eventId, userId);
        return eventService.getEventOfUser(userId, eventId);
    }

    @PatchMapping("/{eventId}")
    public EventFullDto updateEventOfUser(@PathVariable Long userId,
                                          @PathVariable Long eventId,
                                          @Valid @RequestBody UpdateEventUserRequest updateRequest) {
        log.info("Получен запрос на обновление события {} пользователем {}", eventId, userId);
        return eventService.updateEventOfUser(updateRequest, userId, eventId);
    }

    @GetMapping("/{eventId}/requests")
    public List<RequestDto> getRequestsOfUserEvent(@PathVariable Long userId,
                                                   @PathVariable Long eventId) {
        log.info("Получен запрос на получение заявок для события {} пользователя {}", eventId, userId);
        return eventService.getRequestsOfUserEvent(userId, eventId);
    }

    @PatchMapping("/{eventId}/requests")
    public EventRequestStatusUpdateResult updateRequestsStatus(@PathVariable Long userId,
                                                               @PathVariable Long eventId,
                                                               @Valid @RequestBody EventRequestStatusUpdateRequest updateRequest) {
        log.info("Получен запрос на обновление статусов заявок события {} пользователем {}", eventId, userId);
        return eventService.updateRequestsStatus(updateRequest, userId, eventId);
    }
}
