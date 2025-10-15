package ru.practicum.explore.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.client.RestTemplate;
import ru.practicum.explore.client.StatsClient;

@Configuration
public class StatsClientConfig {

    @Value("${stats.service-id:stats-service}")
    private String statsServiceId;

    @Bean
    public StatsClient statsClient(
            DiscoveryClient discoveryClient,
            RetryTemplate retryTemplate,
            RestTemplateBuilder builder
    ) {
        RestTemplate restTemplate = builder.build();
        return new StatsClient(discoveryClient, retryTemplate, restTemplate);
    }
}
