package ru.practicum.explore.user.service;

import ru.practicum.explore.user.dto.NewUserRequest;
import ru.practicum.explore.user.dto.UserDto;

import java.util.List;

public interface UserService {

    List<UserDto> getAllUsers(List<Long> ids, Integer from, Integer size);

    UserDto saveUser(NewUserRequest newUserRequest);

    void deleteUser(Long id);
}