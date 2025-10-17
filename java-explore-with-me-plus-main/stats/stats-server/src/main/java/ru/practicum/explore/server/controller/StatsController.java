package ru.practicum.explore.server.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore.dto.EndHitDto;
import ru.practicum.explore.dto.StatDto;
import ru.practicum.explore.server.service.StatsService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class StatsController {
    private final StatsService statsService;
    private static final String TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

    @PostMapping("/hit")
    @ResponseStatus(HttpStatus.CREATED)
    public String saveHit(@RequestBody @Valid EndHitDto endpointHitDto) {
        log.info("На uri: {} сервиса был отправлен запрос пользователем.", endpointHitDto.getUri());
        try {
            statsService.saveHit(endpointHitDto);
            return "Информация сохранена";
        } catch (Exception e) {
            log.error("saveHit Ошибка: " + e);
            throw e;
        }
    }

    @GetMapping("/stats")
    public List<StatDto> getStats(@RequestParam @DateTimeFormat(pattern = TIME_PATTERN) LocalDateTime start,
                                   @RequestParam @DateTimeFormat(pattern = TIME_PATTERN) LocalDateTime end,
                                   @RequestParam(defaultValue = "") List<String> uris,
                                   @RequestParam(defaultValue = "false") boolean unique) {
        log.info("Поступил запрос на получение статистики запросов c параметрами start: {}, end {}, uris {}, unique {}",
                start, end, uris, unique);
        try {
            return statsService.getStats(start, end, uris, unique);
        } catch (Exception e) {
            log.error("getStats Ошибка: " + e);
            throw e;
        }

    }
}