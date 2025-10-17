package ru.practicum.explore.compilation.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore.compilation.dto.CompilationDto;
import ru.practicum.explore.compilation.service.CompilationService;

import java.util.List;

@RestController
@RequestMapping("/compilations")
@Slf4j
@RequiredArgsConstructor
public class PublicController {

    private final CompilationService compilationService;

    @GetMapping
    public List<CompilationDto> getAllCompilations(@RequestParam(required = false) Boolean pinned,
                                                   @RequestParam(defaultValue = "0") Integer from,
                                                   @RequestParam(defaultValue = "10") Integer size) {
        log.info("Получен запрос на получение подборок событий с параметрами pinned={}, from={}, size={}", pinned, from, size);
        return compilationService.getAllCompilations(pinned, from, size);
    }

    @GetMapping("/{compId}")
    public CompilationDto getCompilationById(@PathVariable Long compId) {
        log.info("Получен запрос на получение подборки событий по id={}", compId);
        return compilationService.getCompilationById(compId);
    }
}
