package com.taskqueue.config;

import com.taskqueue.util.RetryPolicy;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "taskqueue")
@Data
public class TaskQueueConfig {
    private int maxRetries = 3;
    private long retryDelayMs = 5000;
    private boolean deadLetterEnabled = true;
    private SchedulerConfig scheduler = new SchedulerConfig();
    
    @Bean
    public RetryPolicy retryPolicy() {
        return RetryPolicy.builder()
                .maxRetries(maxRetries)
                .initialDelayMs(retryDelayMs)
                .backoffMultiplier(2.0)
                .maxDelayMs(300000)
                .build();
    }
    
    @Data
    public static class SchedulerConfig {
        private boolean enabled = true;
        private String cron = "0 */1 * * * *";
    }
}
