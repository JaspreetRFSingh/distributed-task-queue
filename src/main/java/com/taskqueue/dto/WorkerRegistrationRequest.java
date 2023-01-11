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
public class WorkerRegistrationRequest {
    @NotBlank(message = "Worker ID is required")
    private String workerId;
    
    @NotBlank(message = "Hostname is required")
    private String hostname;
    
    @NotNull(message = "Port is required")
    private Integer port;
}
