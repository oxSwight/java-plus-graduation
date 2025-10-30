package ru.practicum.ewm.stats.server.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.interaction.api.dto.stats.EndpointHitDto;
import ru.practicum.ewm.stats.server.model.EndpointHit;

@UtilityClass
public class DtoMapper {

    public EndpointHit toEndpointHit(EndpointHitDto endpointHitDto) {
        return EndpointHit.builder()
                .app(endpointHitDto.getApp())
                .uri(endpointHitDto.getUri())
                .ip(endpointHitDto.getIp())
                .createdDate(endpointHitDto.getTimestamp())
                .build();
    }
}
