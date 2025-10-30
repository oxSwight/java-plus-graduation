package ru.practicum.user.service.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.interaction.api.dto.user.NewUserRequest;
import ru.practicum.interaction.api.dto.user.UserDto;
import ru.practicum.interaction.api.dto.user.UserDtoForAdmin;
import ru.practicum.interaction.api.dto.user.UserShortDto;
import ru.practicum.user.service.model.User;

@UtilityClass
public class UserMapper {
    public User toUser(NewUserRequest newUserRequest) {
        return User.builder()
                .email(newUserRequest.getEmail())
                .name(newUserRequest.getName())
                .build();
    }

    public UserDto toUserDto(User user) {
        return UserDto.builder()
                .email(user.getEmail())
                .id(user.getId())
                .name(user.getName())
                .build();
    }

    public UserShortDto toUserShortDto(User user) {
        return UserShortDto.builder()
                .id(user.getId())
                .name(user.getName())
                .build();
    }

    public UserDtoForAdmin toUserDtoForAdmin(User user) {
        return UserDtoForAdmin.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }
}
