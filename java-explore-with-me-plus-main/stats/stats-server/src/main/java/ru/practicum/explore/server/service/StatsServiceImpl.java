package ru.practicum.explore.server.service;

import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explore.dto.EndHitDto;
import ru.practicum.explore.dto.StatDto;
import ru.practicum.explore.server.tools.DtoMapper;
import ru.practicum.explore.server.repository.StatsRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StatsServiceImpl implements StatsService {
    private final StatsRepository statsRepository;

    @Override
    public List<StatDto> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique) {
        if (start.isAfter(end)) {
            throw new ValidationException("End не может быть раньше start");
        }

        if (!uris.isEmpty() && unique) {
            return statsRepository.getStatsByUriWithUniqueIp(start, end, uris);
        } else if (uris.isEmpty() && unique) {
            return statsRepository.getStatsWithUniqueIp(start, end);
        } else if (!uris.isEmpty()) {
            return statsRepository.getStatsByUri(start, end, uris);
        } else {
            return statsRepository.getStats(start, end);
        }
    }

    @Transactional
    @Override
    public void saveHit(EndHitDto endpointHitDto) {
        statsRepository.save(DtoMapper.toEndpointHit(endpointHitDto));
    }
}