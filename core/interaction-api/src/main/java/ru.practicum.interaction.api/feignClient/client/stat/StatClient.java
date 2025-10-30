package ru.practicum.interaction.api.feignClient.client.stat;

import feign.FeignException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import ru.practicum.interaction.api.dto.stats.EndpointHitDto;
import ru.practicum.interaction.api.dto.stats.StatsDto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static ru.practicum.interaction.api.utils.date.DateTimeFormat.TIME_PATTERN;

@FeignClient(name = "stats-server")
public interface StatClient {

    @PostMapping("/hit")
    String saveHit(@RequestBody EndpointHitDto endpointHitDto) throws FeignException;

    @CircuitBreaker(name = "defaultBreaker", fallbackMethod = "getStatsFallback")
    @GetMapping("/stats")
    List<StatsDto> getStats(@RequestParam @DateTimeFormat(pattern = TIME_PATTERN) LocalDateTime start,
                            @RequestParam @DateTimeFormat(pattern = TIME_PATTERN) LocalDateTime end,
                            @RequestParam(defaultValue = "") List<String> uris,
                            @RequestParam(defaultValue = "false") boolean unique) throws FeignException;

    @GetMapping("/stats")
    default List<StatsDto> getStatsFallback(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique,
                                            Throwable throwable) {
        return new ArrayList<>();
    }
}
