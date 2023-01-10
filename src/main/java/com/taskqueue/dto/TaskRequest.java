package com.taskqueue.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskRequest {
    @NotBlank(message = "Task type is required")
    private String taskType;
    
    @NotNull(message = "Payload is required")
    private String payload;
    
    @Builder.Default
    private Integer priority = 0;
    
    private String queueName;
    
    @Builder.Default
    private Integer maxRetries = 3;
    
    private String scheduledAt;
}
