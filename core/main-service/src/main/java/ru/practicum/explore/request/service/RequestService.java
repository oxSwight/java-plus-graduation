package ru.practicum.explore.request.service;

import ru.practicum.explore.request.dto.RequestDto;
import ru.practicum.explore.request.model.Request;

import java.util.List;
import java.util.Map;

public interface RequestService {

    RequestDto addRequest(Long userId, Long eventId);

    RequestDto cancelRequest(Long userId, Long requestId);

    List<RequestDto> getAllUserRequests(Long userId);

    Map<Long, List<Request>> prepareConfirmedRequests(List<Long> eventIds);
}