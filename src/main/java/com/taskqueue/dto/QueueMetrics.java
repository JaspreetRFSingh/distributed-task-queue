package com.taskqueue.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QueueMetrics {
    private String queueName;
    private Long pendingTasks;
    private Long runningTasks;
    private Long completedTasks;
    private Long failedTasks;
    private Long deadLetterTasks;
    private Double avgProcessingTimeMs;
    private Double throughputPerMinute;
}
