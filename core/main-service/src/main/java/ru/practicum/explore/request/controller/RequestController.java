package ru.practicum.explore.request.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore.request.dto.RequestDto;
import ru.practicum.explore.request.service.RequestService;

import java.util.List;

@RestController
@RequestMapping("/users/{userId}/requests")
@Slf4j
@RequiredArgsConstructor
public class RequestController {

    private final RequestService requestService;

    @GetMapping
    public List<RequestDto> getAllUserRequests(@PathVariable Long userId) {
        log.info("Получен запрос: пользователь {} хочет получить все свои Request", userId);
        return requestService.getAllUserRequests(userId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RequestDto addRequest(@PathVariable Long userId,
                                 @RequestParam(defaultValue = "0") Long eventId) {
        log.info("Получен запрос: пользователь {} добавляет Request для события {}", userId, eventId);
        return requestService.addRequest(userId, eventId);
    }

    @PatchMapping("/{requestId}/cancel")
    public RequestDto cancelRequest(@PathVariable Long userId, @PathVariable Long requestId) {
        log.info("Получен запрос: пользователь {} отменяет Request {}", userId, requestId);
        return requestService.cancelRequest(userId, requestId);
    }
}
