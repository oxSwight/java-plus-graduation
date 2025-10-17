package ru.practicum.explore.compilation.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.explore.event.dto.EventShortDto;

import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CompilationDto {
    List<EventShortDto> events;
    Long id;
    Boolean pinned;
    String title;
}