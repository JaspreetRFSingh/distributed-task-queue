package com.taskqueue.monitor;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class HealthIndicator implements HealthIndicator {
    private final RedisTemplate<String, String> redisTemplate;
    
    @Override
    public Health health() {
        try {
            redisTemplate.opsForValue().get("health:check");
            return Health.up().build();
        } catch (Exception e) {
            return Health.down(e).build();
        }
    }
}
