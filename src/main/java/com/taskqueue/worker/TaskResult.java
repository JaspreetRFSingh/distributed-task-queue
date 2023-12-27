package com.taskqueue.worker;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskResult {
    private boolean success;
    private String errorMessage;
    private Map<String, Object> output;
    private LocalDateTime completedAt;
    private long durationMs;
}
