package ru.practicum.ewm.server.service;

import ru.practicum.ewm.dto.EndHitDto;
import ru.practicum.ewm.dto.StatDto;

import java.time.LocalDateTime;
import java.util.List;

public interface StatsService {

    List<StatDto> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique);

    void saveHit(EndHitDto endpointHitDto);
}