package com.ezreal.small.payment.config;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

/**
 * @author Ezreal
 * @Date 2024/11/16
 */
@Configuration
public class CaffeineCacheConfig {

    @Bean("accessTokenCache")
    public Cache<String, String> accessTokenCache() {
        return Caffeine.newBuilder()
                .maximumSize(1)
                .expireAfterWrite(1, TimeUnit.HOURS)
                .build();
    }

    @Bean("ticketCache")
    public Cache<String, String> ticketCache() {
        return Caffeine.newBuilder()
                .maximumSize(1)
                .expireAfterWrite(1, TimeUnit.HOURS)
                .build();
    }
}
