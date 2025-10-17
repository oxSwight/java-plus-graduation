package ru.practicum.ewm.server.service;

import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.dto.EndHitDto;
import ru.practicum.ewm.dto.StatDto;
import ru.practicum.ewm.server.repository.StatsRepository;
import ru.practicum.ewm.server.tools.DtoMapper;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StatsServiceImpl implements StatsService {

    private final StatsRepository statsRepository;

    @Override
    public List<StatDto> getStats(LocalDateTime start,
                                  LocalDateTime end,
                                  List<String> uris,
                                  boolean unique) {

        if (start.isAfter(end)) {
            throw new ValidationException("Параметр 'end' не может быть раньше 'start'.");
        }

        if (unique) {
            return uris.isEmpty()
                    ? statsRepository.getStatsWithUniqueIp(start, end)
                    : statsRepository.getStatsByUriWithUniqueIp(start, end, uris);
        }

        return uris.isEmpty()
                ? statsRepository.getStats(start, end)
                : statsRepository.getStatsByUri(start, end, uris);
    }

    @Override
    @Transactional
    public void saveHit(EndHitDto endpointHitDto) {
        statsRepository.save(DtoMapper.toEndpointHit(endpointHitDto));
    }
}
