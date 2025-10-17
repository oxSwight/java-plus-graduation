package ru.practicum.explore.event.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import ru.practicum.explore.request.dto.RequestDto;

import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class EventRequestStatusUpdateResult {
    List<RequestDto> confirmedRequests;
    List<RequestDto> rejectedRequests;
}