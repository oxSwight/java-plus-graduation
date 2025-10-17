package ru.practicum.explore.compilation.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explore.compilation.dto.CompilationDto;
import ru.practicum.explore.compilation.dto.NewCompilationDto;
import ru.practicum.explore.compilation.dto.UpdateCompRequest;
import ru.practicum.explore.compilation.mapper.CompilationMapper;
import ru.practicum.explore.compilation.model.Compilation;
import ru.practicum.explore.compilation.repository.CompilationRepository;
import ru.practicum.explore.dto.StatDto;
import ru.practicum.explore.event.dto.EventShortDto;
import ru.practicum.explore.event.mapper.EventMapper;
import ru.practicum.explore.event.model.Event;
import ru.practicum.explore.event.repository.EventRepository;
import ru.practicum.explore.exception.NotFoundException;
import ru.practicum.explore.stats.client.StatClient;

import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CompilationServiceImpl implements CompilationService {

    private final CompilationRepository compilationRepository;
    private final EventRepository eventRepository;
    private final StatClient statClient;

    @Transactional
    @Override
    public CompilationDto addCompilation(NewCompilationDto newCompilationDto) {
        List<Event> events = Optional.ofNullable(newCompilationDto.getEvents())
                .filter(ids -> !ids.isEmpty())
                .map(ids -> getEventsByIds(new ArrayList<>(ids)))
                .orElse(Collections.emptyList());

        boolean pinned = Optional.ofNullable(newCompilationDto.getPinned()).orElse(false);
        newCompilationDto.setPinned(pinned);

        Compilation compilation = compilationRepository.save(CompilationMapper.toCompilation(newCompilationDto, events));
        return CompilationMapper.toCompilationDto(compilation, mapToEventShort(events));
    }

    @Transactional
    @Override
    public CompilationDto updateCompilation(Long compId, UpdateCompRequest updateRequest) {
        Compilation compilation = compilationRepository.findById(compId)
                .orElseThrow(() -> new NotFoundException("Подборка с id=" + compId + " не найдена"));

        Optional.ofNullable(updateRequest.getEvents())
                .filter(ids -> !ids.isEmpty())
                .ifPresent(ids -> compilation.setEvents(new HashSet<>(getEventsByIds(new ArrayList<>(ids)))));

        Optional.ofNullable(updateRequest.getPinned()).ifPresent(compilation::setPinned);
        Optional.ofNullable(updateRequest.getTitle())
                .filter(title -> !title.isBlank())
                .ifPresent(compilation::setTitle);

        return CompilationMapper.toCompilationDto(compilation, mapToEventShort(new ArrayList<>(compilation.getEvents())));
    }

    @Transactional
    @Override
    public void deleteCompilation(Long compId) {
        if (!compilationRepository.existsById(compId)) {
            throw new NotFoundException("Подборка с id=" + compId + " не найдена");
        }
        compilationRepository.deleteById(compId);
    }

    @Override
    public List<CompilationDto> getAllCompilations(Boolean pinned, Integer from, Integer size) {
        PageRequest page = PageRequest.of(from / size, size);
        List<Compilation> compilations = pinned == null
                ? compilationRepository.findAll(page).toList()
                : compilationRepository.findAllByPinned(page, pinned);

        Map<Long, EventShortDto> eventDtoCache = mapToEventShort(compilations.stream()
                .flatMap(c -> c.getEvents().stream())
                .toList())
                .stream()
                .collect(Collectors.toMap(EventShortDto::getId, Function.identity(), (a, b) -> a));

        return compilations.stream()
                .map(c -> CompilationMapper.toCompilationDto(
                        c,
                        c.getEvents().stream()
                                .map(e -> eventDtoCache.get(e.getId()))
                                .filter(Objects::nonNull)
                                .toList()
                ))
                .toList();
    }

    @Override
    public CompilationDto getCompilationById(Long compId) {
        Compilation compilation = compilationRepository.findById(compId)
                .orElseThrow(() -> new NotFoundException("Подборка с id=" + compId + " не найдена"));
        return CompilationMapper.toCompilationDto(compilation, mapToEventShort(new ArrayList<>(compilation.getEvents())));
    }

    private List<Event> getEventsByIds(List<Long> ids) {
        List<Event> events = eventRepository.findAllByIdIn(ids);
        if (events.size() != ids.size()) {
            throw new NotFoundException("Некоторые события не найдены");
        }
        return events;
    }

    private List<EventShortDto> mapToEventShort(List<Event> events) {
        if (events.isEmpty()) {
            return Collections.emptyList();
        }

        LocalDateTime minTime = events.stream()
                .map(Event::getCreatedOn)
                .min(Comparator.naturalOrder())
                .orElse(LocalDateTime.now());

        List<String> uris = events.stream()
                .map(e -> "/events/" + e.getId())
                .toList();

        List<StatDto> stats = statClient.getStats(minTime.minusSeconds(1), LocalDateTime.now(), uris, false);

        Map<String, Long> hitsMap = stats.stream()
                .collect(Collectors.toMap(StatDto::getUri, StatDto::getHits, (a, b) -> a));

        return events.stream()
                .map(e -> EventMapper.mapToShortDto(e, hitsMap.getOrDefault("/events/" + e.getId(), 0L)))
                .toList();
    }
}
