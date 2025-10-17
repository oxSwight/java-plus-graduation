package ru.practicum.ewm.request.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.request.model.Request;
import ru.practicum.ewm.user.model.User;

import java.util.List;
import java.util.Optional;

public interface RequestRepository extends JpaRepository<Request, Long> {

    Boolean existsByRequesterAndEvent(User requester, Event event);

    Optional<Request> findByRequesterIdAndId(Long requesterId, Long requestId);

    List<Request> findAllByRequesterId(Long requesterId);

    List<Request> findAllByEventInitiatorIdAndEventId(Long userId, Long eventId);

    @Query("select p from Request p where p.status = 'CONFIRMED' and p.event.id in ?1")
    List<Request> findConfirmedRequests(List<Long> ids);
}