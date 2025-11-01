package ru.practicum.event.service.event.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.interaction.api.enums.event.State;
import ru.practicum.event.service.category.mapper.CategoryMapper;
import ru.practicum.event.service.category.model.Category;
import ru.practicum.event.service.event.model.Event;
import ru.practicum.interaction.api.dto.event.EventFullDto;
import ru.practicum.interaction.api.dto.event.EventShortDto;
import ru.practicum.interaction.api.dto.event.Location;
import ru.practicum.interaction.api.dto.event.NewEventDto;

import java.time.LocalDateTime;

@UtilityClass
public class EventMapper {

    public Event mapToEvent(NewEventDto eventDto, Category category, Long initiatorId) {
        return Event.builder()
                .eventDate(eventDto.getEventDate())
                .annotation(eventDto.getAnnotation())
                .paid(eventDto.getPaid())
                .category(category)
                .confirmedRequests(0)
                .createdOn(LocalDateTime.now())
                .description(eventDto.getDescription())
                .state(State.PENDING)
                .title(eventDto.getTitle())
                .lat(eventDto.getLocation().getLat())
                .lon(eventDto.getLocation().getLon())
                .participantLimit(eventDto.getParticipantLimit())
                .requestModeration(eventDto.getRequestModeration())
                .initiatorId(initiatorId)
                .commenting(eventDto.getCommenting())
                .build();
    }

    public EventFullDto mapToFullDto(Event event, Long views) {
        return EventFullDto.builder()
                .id(event.getId())
                .annotation(event.getAnnotation())
                .category(CategoryMapper.toCategoryDto(event.getCategory()))
                .confirmedRequests(event.getConfirmedRequests())
                .createdOn(event.getCreatedOn())
                .publishedOn(event.getPublishedOn())
                .description(event.getDescription())
                .eventDate(event.getEventDate())
                .initiator(event.getInitiatorId())
                .location(Location.builder().lat(event.getLat()).lon(event.getLon()).build())
                .paid(event.getPaid())
                .views(views)
                .participantLimit(event.getParticipantLimit())
                .requestModeration(event.getRequestModeration())
                .state(event.getState())
                .title(event.getTitle())
                .commenting(event.getCommenting())
                .build();
    }

    public EventShortDto mapToShortDto(Event event, Long views) {
        return EventShortDto.builder()
                .annotation(event.getAnnotation())
                .category(CategoryMapper.toCategoryDto(event.getCategory()))
                .confirmedRequests(event.getConfirmedRequests())
                .eventDate(event.getEventDate())
                .publishedOn(event.getPublishedOn())
                .id(event.getId())
                .initiator(event.getInitiatorId())
                .paid(event.getPaid())
                .title(event.getTitle())
                .views(views)
                .commenting(event.getCommenting())
                .build();
    }
}
