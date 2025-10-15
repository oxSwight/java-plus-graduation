package ru.practicum.explore.client;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.backoff.FixedBackOffPolicy;
import org.springframework.retry.policy.MaxAttemptsRetryPolicy;
import org.springframework.retry.support.RetryTemplate;

@Configuration
public class RetryConfig {
    @Bean
    public RetryTemplate retryTemplate() {
        RetryTemplate t = new RetryTemplate();
        FixedBackOffPolicy backoff = new FixedBackOffPolicy();
        backoff.setBackOffPeriod(3000L);
        t.setBackOffPolicy(backoff);

        MaxAttemptsRetryPolicy p = new MaxAttemptsRetryPolicy();
        p.setMaxAttempts(3);
        t.setRetryPolicy(p);
        return t;
    }
}
