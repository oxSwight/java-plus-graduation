package ru.practicum.explore.event.service;

import jakarta.servlet.http.HttpServletRequest;
import ru.practicum.explore.event.dto.*;
import ru.practicum.explore.request.dto.RequestDto;

import java.util.List;

public interface EventService {

    EventFullDto addEvent(NewEventDto eventDto, Long userId);

    List<EventShortDto> getEventsOfUser(Long userId, Integer from, Integer size);

    EventFullDto getEventOfUser(Long userId, Long eventId);

    EventFullDto updateEventOfUser(UpdateEventUserRequest updateRequest, Long userId, Long eventId);

    List<EventShortDto> getPublicEventsByFilter(HttpServletRequest httpServletRequest, EventPublicFilter inputFilter);

    EventFullDto getPublicEventById(HttpServletRequest httpServletRequest, Long id);

    List<EventFullDto> getEventsForAdmin(EventAdminFilter admin);

    EventFullDto updateEventAdmin(Long eventId, UpdateEventAdminRequest updateEventAdminRequest);

    List<RequestDto> getRequestsOfUserEvent(Long userId, Long eventId);

    EventRequestStatusUpdateResult updateRequestsStatus(EventRequestStatusUpdateRequest updateRequest, Long userId,
                                                        Long eventId);
}