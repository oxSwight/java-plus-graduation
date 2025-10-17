package ru.practicum.explore.user.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.explore.user.model.User;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

    Page<User> findAllByIdIn(List<Long> ids, PageRequest pageRequest);

    Boolean existsByEmail(String email);
}