package com.poc.rest_api_poc.config;

import io.github.resilience4j.ratelimiter.RateLimiterConfig;
import io.github.resilience4j.ratelimiter.RateLimiterRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class Resilience4jConfig {

    @Bean
    public RateLimiterRegistry rateLimiterRegistry() {
        // Create a custom configuration for a RateLimiter
        RateLimiterConfig config = RateLimiterConfig.custom()
                .timeoutDuration(Duration.ofSeconds(0)) // wait time for thread
                .limitRefreshPeriod(Duration.ofSeconds(4)) // this is the window
                .limitForPeriod(4) // this is number of request api will process
                .build();

        // Create a RateLimiterRegistry with a custom global configuration
        return RateLimiterRegistry.of(config);
    }
}