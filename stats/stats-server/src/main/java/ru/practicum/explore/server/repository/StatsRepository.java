package ru.practicum.explore.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.explore.dto.StatDto;
import ru.practicum.explore.server.model.EndpointHit;

import java.time.LocalDateTime;
import java.util.List;

public interface StatsRepository extends JpaRepository<EndpointHit, Long> {

    @Query("""
           SELECT new ru.practicum.explore.dto.StatDto(r.app, r.uri, COUNT(DISTINCT r.ip))
           FROM EndpointHit AS r
           WHERE r.createdDate BETWEEN :start AND :end
             AND r.uri IN (:uris)
           GROUP BY r.app, r.uri
           """)
    List<StatDto> getStatsByUriWithUniqueIp(@Param("start") LocalDateTime start,
                                            @Param("end") LocalDateTime end,
                                            @Param("uris") List<String> uris);

    @Query("""
           SELECT new ru.practicum.explore.dto.StatDto(r.app, r.uri, COUNT(DISTINCT r.ip))
           FROM EndpointHit AS r
           WHERE r.createdDate BETWEEN :start AND :end
           GROUP BY r.app, r.uri
           """)
    List<StatDto> getStatsWithUniqueIp(@Param("start") LocalDateTime start,
                                       @Param("end") LocalDateTime end);

    @Query("""
           SELECT new ru.practicum.explore.dto.StatDto(r.app, r.uri, COUNT(r))
           FROM EndpointHit AS r
           WHERE r.createdDate BETWEEN :start AND :end
             AND r.uri IN (:uris)
           GROUP BY r.app, r.uri
           ORDER BY COUNT(r) DESC
           """)
    List<StatDto> getStatsByUri(@Param("start") LocalDateTime start,
                                @Param("end") LocalDateTime end,
                                @Param("uris") List<String> uris);

    @Query("""
           SELECT new ru.practicum.explore.dto.StatDto(r.app, r.uri, COUNT(r))
           FROM EndpointHit AS r
           WHERE r.createdDate BETWEEN :start AND :end
           GROUP BY r.app, r.uri
           """)
    List<StatDto> getStats(@Param("start") LocalDateTime start,
                           @Param("end") LocalDateTime end);
}
