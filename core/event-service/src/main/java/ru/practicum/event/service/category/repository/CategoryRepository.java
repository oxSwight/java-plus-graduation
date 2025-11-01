package ru.practicum.event.service.category.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.event.service.category.model.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    boolean existsByName(String name);
}
