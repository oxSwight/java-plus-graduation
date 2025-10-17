package ru.practicum.explore.config;

import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients(basePackages = "ru.practicum.explore.client")
@EnableDiscoveryClient
public class StatsClientConfig {
}
