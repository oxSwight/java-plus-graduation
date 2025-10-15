package ru.practicum.explore.client;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import ru.practicum.explore.dto.EndHitDto;
import ru.practicum.explore.dto.StatDto;

import java.net.URI;
import java.util.List;

@Component
@RequiredArgsConstructor
public class StatsClient {

    private final DiscoveryClient discoveryClient;
    private final RetryTemplate retryTemplate;
    private final RestTemplate restTemplate;

    @Value("${stats.service-id:stats-service}")
    private String statsServiceId;

    private ServiceInstance getInstance() {
        try {
            return discoveryClient.getInstances(statsServiceId).getFirst();
        } catch (Exception e) {
            throw new IllegalStateException("Ошибка обнаружения stats-service", e);
        }
    }

    private URI makeUri(String path) {
        ServiceInstance instance = retryTemplate.execute(ctx -> getInstance());
        return URI.create("http://" + instance.getHost() + ":" + instance.getPort() + path);
    }

    public ResponseEntity<Object> save(EndHitDto dto) {
        return restTemplate.postForEntity(makeUri("/hit"), dto, Object.class);
    }

    public List<StatDto> getStats(String start, String end, List<String> uris, boolean unique) {
        UriComponentsBuilder b = UriComponentsBuilder.fromUri(makeUri("/stats"))
                .queryParam("start", start)
                .queryParam("end", end)
                .queryParam("unique", unique);

        if (uris != null && !uris.isEmpty()) {
            uris.forEach(u -> b.queryParam("uris", u));
        }

        ResponseEntity<List<StatDto>> resp = restTemplate.exchange(
                b.build(true).toUri(),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<StatDto>>() {}
        );
        return resp.getBody();
    }
}
