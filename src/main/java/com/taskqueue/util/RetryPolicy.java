package com.taskqueue.util;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RetryPolicy {
    private int maxRetries;
    private long initialDelayMs;
    private double backoffMultiplier;
    private long maxDelayMs;
    
    public long getDelayForAttempt(int attempt) {
        long delay = (long) (initialDelayMs * Math.pow(backoffMultiplier, attempt));
        return Math.min(delay, maxDelayMs);
    }
}
