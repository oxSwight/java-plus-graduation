package ru.practicum.explore.server.tools;

import lombok.experimental.UtilityClass;
import ru.practicum.explore.dto.EndHitDto;
import ru.practicum.explore.server.model.EndpointHit;

@UtilityClass
public class DtoMapper {

    public EndpointHit toEndpointHit(EndHitDto endpointHitDto) {
        return EndpointHit.builder()
                .app(endpointHitDto.getApp())
                .uri(endpointHitDto.getUri())
                .ip(endpointHitDto.getIp())
                .createdDate(endpointHitDto.getTimestamp())
                .build();
    }
}