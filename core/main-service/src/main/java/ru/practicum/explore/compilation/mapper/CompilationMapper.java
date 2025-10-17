package ru.practicum.explore.compilation.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.explore.compilation.dto.CompilationDto;
import ru.practicum.explore.compilation.dto.NewCompilationDto;
import ru.practicum.explore.compilation.model.Compilation;
import ru.practicum.explore.event.dto.EventShortDto;
import ru.practicum.explore.event.model.Event;

import java.util.HashSet;
import java.util.List;

@UtilityClass
public class CompilationMapper {

    public Compilation toCompilation(NewCompilationDto compilationDto, List<Event> events) {
        return Compilation.builder()
                .pinned(compilationDto.getPinned())
                .title(compilationDto.getTitle())
                .events(new HashSet<>(events))
                .build();
    }

    public CompilationDto toCompilationDto(Compilation compilation, List<EventShortDto> listEventDto) {
        return CompilationDto.builder()
                .id(compilation.getId())
                .events(listEventDto)
                .pinned(compilation.getPinned())
                .title(compilation.getTitle())
                .build();


    }
}