package ru.practicum.interaction.api.feignClient.client.user;
import feign.FeignException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import ru.practicum.interaction.api.dto.user.NewUserRequest;
import ru.practicum.interaction.api.dto.user.UserDto;
import ru.practicum.interaction.api.dto.user.UserDtoForAdmin;

import java.util.Collections;
import java.util.List;


@FeignClient(name = "user-service", path = "/admin/users")
public interface UserClient {

    @CircuitBreaker(name = "defaultBreaker", fallbackMethod = "getAllUsersFallback")
    @GetMapping
    List<UserDto> getAllUsers(@RequestParam(defaultValue = "") List<Long> ids,
                              @PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
                              @Positive @RequestParam(defaultValue = "10") Integer size) throws FeignException;

    @PostMapping
    UserDto saveUser(@RequestBody @Valid NewUserRequest newUserRequest) throws FeignException;

    @DeleteMapping("/{userId}")
    void deleteUser(@PathVariable Long userId) throws FeignException;

    @GetMapping("/{userId}")
    UserDto findById(@PathVariable Long userId) throws FeignException;

    @GetMapping("/admin/{userId}")
    UserDtoForAdmin adminFindById(@PathVariable Long userId) throws FeignException;

    @GetMapping
    default List<UserDto> getAllUsersFallback(List<Long> ids, Integer from, Integer size, Throwable throwable) {
        return Collections.emptyList();
    }
}