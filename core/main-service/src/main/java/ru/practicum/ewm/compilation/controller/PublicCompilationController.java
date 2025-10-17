package ru.practicum.ewm.compilation.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.compilation.dto.CompilationDto;
import ru.practicum.ewm.compilation.service.CompilationService;

import java.util.List;

@RestController
@RequestMapping("/compilations")
@Slf4j
@RequiredArgsConstructor
public class PublicCompilationController {
    private final CompilationService compilationService;

    @GetMapping
    public List<CompilationDto> getAllCompilations(@RequestParam(required = false) Boolean pinned,
                                                   @RequestParam(defaultValue = "0") Integer from,
                                                   @RequestParam(defaultValue = "10") Integer size) {
        try {
            log.info("Поступил запрос на получение всех подборок событий");
            return compilationService.getAllCompilations(pinned, from, size);
        } catch (Exception e) {
            log.error("getAllCompilations Ошибка: " + e);
            throw e;
        }

    }

    @GetMapping("/{compId}")
    public CompilationDto getCompilationById(@PathVariable Long compId) {
        try {
            log.info("Поступил запрос на получение подборки событий по id");
            return compilationService.getCompilationById(compId);
        } catch (Exception e) {
            log.error("getCompilationById Ошибка: " + e);
            throw e;
        }
    }
}
