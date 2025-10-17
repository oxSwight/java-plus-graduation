package ru.practicum.ewm.stats.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.ewm.stats.dto.StatsDto;
import ru.practicum.ewm.stats.server.model.EndpointHit;

import java.time.LocalDateTime;
import java.util.List;

public interface StatsRepository extends JpaRepository<EndpointHit, Long> {

    @Query("""
           SELECT new ru.practicum.explore.dto.StatsDto(r.app, r.uri, COUNT(DISTINCT r.ip))
           FROM EndpointHit AS r
           WHERE r.createdDate BETWEEN :start AND :end
             AND r.uri IN (:uris)
           GROUP BY r.app, r.uri
           """)
    List<StatsDto> getStatsByUriWithUniqueIp(@Param("start") LocalDateTime start,
                                             @Param("end") LocalDateTime end,
                                             @Param("uris") List<String> uris);

    @Query("""
           SELECT new ru.practicum.explore.dto.StatsDto(r.app, r.uri, COUNT(DISTINCT r.ip))
           FROM EndpointHit AS r
           WHERE r.createdDate BETWEEN :start AND :end
           GROUP BY r.app, r.uri
           """)
    List<StatsDto> getStatsWithUniqueIp(@Param("start") LocalDateTime start,
                                       @Param("end") LocalDateTime end);

    @Query("""
           SELECT new ru.practicum.explore.dto.StatsDto(r.app, r.uri, COUNT(r))
           FROM EndpointHit AS r
           WHERE r.createdDate BETWEEN :start AND :end
             AND r.uri IN (:uris)
           GROUP BY r.app, r.uri
           ORDER BY COUNT(r) DESC
           """)
    List<StatsDto> getStatsByUri(@Param("start") LocalDateTime start,
                                @Param("end") LocalDateTime end,
                                @Param("uris") List<String> uris);

    @Query("""
           SELECT new ru.practicum.explore.dto.StatsDto(r.app, r.uri, COUNT(r))
           FROM EndpointHit AS r
           WHERE r.createdDate BETWEEN :start AND :end
           GROUP BY r.app, r.uri
           """)
    List<StatsDto> getStats(@Param("start") LocalDateTime start,
                           @Param("end") LocalDateTime end);
}
