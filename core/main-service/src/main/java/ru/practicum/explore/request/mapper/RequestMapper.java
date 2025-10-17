package ru.practicum.explore.request.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.explore.request.dto.RequestDto;
import ru.practicum.explore.request.model.Request;

import java.util.List;

@UtilityClass
public class RequestMapper {

    public RequestDto toParticipationRequestDto(Request request) {
        return RequestDto.builder()
                .id(request.getId())
                .requester(request.getRequester().getId())
                .event(request.getEvent().getId())
                .created(request.getCreated())
                .status(request.getStatus())
                .build();
    }

    public List<RequestDto> toParticipationRequestDto(List<Request> requests) {
        return requests.stream().map(RequestMapper::toParticipationRequestDto).toList();
    }
}