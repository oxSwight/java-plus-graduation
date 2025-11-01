package ru.practicum.event.service.category.service;

import ru.practicum.interaction.api.dto.category.CategoryDto;
import ru.practicum.interaction.api.dto.category.NewCategoryDto;

import java.util.List;

public interface CategoryService {

    CategoryDto createCategory(NewCategoryDto newCategoryDto);

    void deleteCategory(Long catId);

    CategoryDto updateCategory(CategoryDto categoryDto, Long catId);

    List<CategoryDto> getCategories(Integer from, Integer size);

    CategoryDto getCategoryById(Long catId);
}
