package ru.practicum.event.service.category.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.event.service.category.mapper.CategoryMapper;
import ru.practicum.event.service.category.model.Category;
import ru.practicum.event.service.category.repository.CategoryRepository;
import ru.practicum.event.service.event.repository.EventRepository;
import ru.practicum.interaction.api.dto.category.CategoryDto;
import ru.practicum.interaction.api.dto.category.NewCategoryDto;
import ru.practicum.interaction.api.exception.ConflictDataException;
import ru.practicum.interaction.api.exception.DuplicateException;
import ru.practicum.interaction.api.exception.NotFoundException;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final EventRepository eventRepository;

    @Transactional
    @Override
    public CategoryDto createCategory(NewCategoryDto newCategoryDto) {
        String categoryName = newCategoryDto.getName();

        if (categoryRepository.existsByName(categoryName)) {
            throw new DuplicateException("Категория с таким именем уже существует: " + categoryName);
        }

        Category category = CategoryMapper.toCategory(newCategoryDto);
        Category savedCategory = categoryRepository.save(category);
        return CategoryMapper.toCategoryDto(savedCategory);
    }

    @Transactional
    @Override
    public void deleteCategory(Long catId) {
        if (!categoryRepository.existsById(catId)) {
            throw new NotFoundException("Категория не найдена");
        }
        if (eventRepository.existsByCategoryId(catId)) {
            throw new ConflictDataException("Нельзя удалить категорию с привязанными событиями");
        }
        categoryRepository.deleteById(catId);
    }

    @Transactional
    @Override
    public CategoryDto updateCategory(CategoryDto categoryDto, Long catId) {
        Category category = checkCategory(catId);

        if (!category.getName().equals(categoryDto.getName())) {
            if (categoryRepository.existsByName(categoryDto.getName())) {
                throw new DuplicateException("Категория с именем '" + categoryDto.getName() + "' уже существует");
            }
            category.setName(categoryDto.getName());
        }

        Category updatedCategory = categoryRepository.save(category);
        return CategoryMapper.toCategoryDto(updatedCategory);
    }

    @Override
    public List<CategoryDto> getCategories(Integer from, Integer size) {
        if (from == null || size == null || from < 0 || size <= 0) {
            throw new IllegalArgumentException("Некорректные параметры пагинации: from=" + from + ", size=" + size);
        }

        PageRequest pageRequest = PageRequest.of(from / size, size);
        return categoryRepository.findAll(pageRequest)
                .stream()
                .map(CategoryMapper::toCategoryDto)
                .toList();
    }

    @Override
    public CategoryDto getCategoryById(Long catId) {
        return CategoryMapper.toCategoryDto(checkCategory(catId));
    }

    private Category checkCategory(Long catId) {
        return categoryRepository.findById(catId)
                .orElseThrow(() -> new NotFoundException("Категория не найдена или недоступна"));
    }
}