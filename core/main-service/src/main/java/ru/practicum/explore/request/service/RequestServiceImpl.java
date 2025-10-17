package ru.practicum.explore.request.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explore.event.enums.State;
import ru.practicum.explore.event.model.Event;
import ru.practicum.explore.event.repository.EventRepository;
import ru.practicum.explore.exception.ConflictDataException;
import ru.practicum.explore.exception.DuplicateException;
import ru.practicum.explore.exception.NotFoundException;
import ru.practicum.explore.exception.ValidationException;
import ru.practicum.explore.request.dto.RequestDto;
import ru.practicum.explore.request.enums.Status;
import ru.practicum.explore.request.mapper.RequestMapper;
import ru.practicum.explore.request.model.Request;
import ru.practicum.explore.request.repository.RequestRepository;
import ru.practicum.explore.user.model.User;
import ru.practicum.explore.user.repository.UserRepository;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RequestServiceImpl implements RequestService {

    private final RequestRepository requestRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;

    @Transactional
    @Override
    public RequestDto addRequest(Long userId, Long eventId) {
        if (eventId == 0) {
            throw new ValidationException("Не задано id события");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id " + userId + " не найден"));
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Событие с id " + eventId + " не найдено"));

        if (requestRepository.existsByRequesterAndEvent(user, event)) {
            throw new DuplicateException("Такой запрос уже существует");
        }
        if (event.getInitiator().getId().equals(user.getId())) {
            throw new ConflictDataException("Пользователь не может участвовать в своём же событии");
        }
        if (event.getState() != State.PUBLISHED) {
            throw new ConflictDataException("Нельзя участвовать в неопубликованном событии");
        }

        int participantLimit = event.getParticipantLimit();
        int confirmedRequests = event.getConfirmedRequests();

        if (participantLimit != 0 && confirmedRequests >= participantLimit) {
            throw new ConflictDataException("Лимит запросов на участие уже достигнут");
        }

        Status status;
        if (participantLimit == 0 || !event.getRequestModeration()) {
            status = Status.CONFIRMED;
            event.setConfirmedRequests(confirmedRequests + 1);
        } else {
            status = Status.PENDING;
        }

        Request request = Request.builder()
                .requester(user)
                .event(event)
                .status(status)
                .build();

        return RequestMapper.toParticipationRequestDto(requestRepository.save(request));
    }

    @Transactional
    @Override
    public RequestDto cancelRequest(Long userId, Long requestId) {
        checkExistsUserById(userId);

        Request request = requestRepository.findByRequesterIdAndId(userId, requestId)
                .orElseThrow(() -> new NotFoundException(
                        "У пользователя с id " + userId + " не найден запрос с id " + requestId));

        if (request.getStatus() == Status.CONFIRMED) {
            Event event = request.getEvent();
            event.setConfirmedRequests(event.getConfirmedRequests() - 1);
        }

        request.setStatus(Status.CANCELED);
        return RequestMapper.toParticipationRequestDto(request);
    }

    @Override
    public List<RequestDto> getAllUserRequests(Long userId) {
        checkExistsUserById(userId);
        return RequestMapper.toParticipationRequestDto(requestRepository.findAllByRequesterId(userId));
    }

    @Override
    public Map<Long, List<Request>> prepareConfirmedRequests(List<Long> eventIds) {
        log.info("Получаем список подтверждённых запросов для событий: {}", eventIds);

        List<Request> confirmedRequests = requestRepository.findConfirmedRequests(eventIds);
        Map<Long, List<Request>> result = new HashMap<>();

        for (Request request : confirmedRequests) {
            result.computeIfAbsent(request.getEvent().getId(), id -> new ArrayList<>()).add(request);
        }

        return result;
    }

    private void checkExistsUserById(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("Пользователь с id " + userId + " не найден");
        }
    }
}
