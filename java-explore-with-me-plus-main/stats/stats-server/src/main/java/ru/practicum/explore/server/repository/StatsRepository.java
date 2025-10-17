package ru.practicum.explore.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.explore.dto.StatDto;
import ru.practicum.explore.server.model.EndpointHit;

import java.time.LocalDateTime;
import java.util.List;

public interface StatsRepository extends JpaRepository<EndpointHit, Long> {

    @Query("select new ru.practicum.ewm.stats.dto.StatsDto(r.app, r.uri, count(distinct(r.ip))) " +
            "from EndpointHit AS r " +
            "where r.createdDate >= :start AND r.createdDate <= :end AND uri in (:uris) " +
            "group by r.app, r.uri")
    List<StatDto> getStatsByUriWithUniqueIp(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end,
                                             @Param("uris") List<String> uris);

    @Query("select new ru.practicum.ewm.stats.dto.StatsDto(r.app, r.uri, count(distinct(r.ip))) " +
            "from EndpointHit AS r " +
            "where r.createdDate >= :start AND r.createdDate <= :end " +
            "group by r.app, r.uri")
    List<StatDto> getStatsWithUniqueIp(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    @Query("select new ru.practicum.ewm.stats.dto.StatsDto(r.app, r.uri, count(r)) " +
            "from EndpointHit AS r " +
            "where r.createdDate >= :start AND r.createdDate <= :end AND uri in (:uris) " +
            "group by r.app, r.uri " +
            "order by count(r) desc")
    List<StatDto> getStatsByUri(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end,
                                 @Param("uris") List<String> uris);

    @Query("select new ru.practicum.ewm.stats.dto.StatsDto(r.app, r.uri, count(r)) " +
            "from EndpointHit AS r " +
            "where r.createdDate >= :start AND r.createdDate <= :end " +
            "group by r.app, r.uri")
    List<StatDto> getStats(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);
}