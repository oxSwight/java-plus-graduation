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

    /**
     * POST /hit — сохранение информации о запросе.
     *
     * @param endpointHitDto данные о запросе
     * @return сообщение о сохранении
     */
    @PostMapping("/hit")
    @ResponseStatus(HttpStatus.CREATED)
    public String saveHit(@Valid @RequestBody EndHitDto endpointHitDto) {
        log.info("Получен запрос на сохранение хита по URI: {}", endpointHitDto.getUri());
        statsService.saveHit(endpointHitDto);
        return "Информация сохранена";
    }

    /**
     * GET /stats — получение статистики запросов.
     *
     * @param start  начало диапазона
     * @param end    конец диапазона
     * @param uris   список URI для фильтрации
     * @param unique учитывать только уникальные IP
     * @return список статистических данных
     */
    @GetMapping("/stats")
    public List<StatDto> getStats(
            @RequestParam @DateTimeFormat(pattern = TIME_PATTERN) LocalDateTime start,
            @RequestParam @DateTimeFormat(pattern = TIME_PATTERN) LocalDateTime end,
            @RequestParam(defaultValue = "") List<String> uris,
            @RequestParam(defaultValue = "false") boolean unique) {

        log.info("Запрос статистики: start={}, end={}, uris={}, unique={}", start, end, uris, unique);
        return statsService.getStats(start, end, uris, unique);
    }
}
