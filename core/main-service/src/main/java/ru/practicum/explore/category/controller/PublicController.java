package ru.practicum.explore.category.controller;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore.category.dto.CategoryDto;
import ru.practicum.explore.category.service.CategoryService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/categories")
@Slf4j
public class PublicController {

    private final CategoryService categoryService;

    @GetMapping
    public List<CategoryDto> getCategories(
            @PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
            @Positive @RequestParam(defaultValue = "10") Integer size) {
        log.info("Запрос списка категорий: from={}, size={}", from, size);
        return categoryService.getCategories(from, size);
    }

    @GetMapping("/{catId}")
    public CategoryDto getCategoryById(@PathVariable Long catId) {
        log.info("Запрос категории по id={}", catId);
        return categoryService.getCategoryById(catId);
    }
}
