package ru.practicum.ewm.server.tools;

import lombok.experimental.UtilityClass;
import ru.practicum.ewm.dto.EndHitDto;
import ru.practicum.ewm.server.model.EndpointHit;

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