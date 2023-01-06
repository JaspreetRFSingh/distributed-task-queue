package com.taskqueue.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "workers")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Worker {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    
    @Column(name = "worker_id", unique = true, nullable = false)
    private String workerId;
    
    @Column(nullable = false)
    private String hostname;
    
    @Column(nullable = false)
    private Integer port;
    
    @Column(nullable = false)
    private WorkerStatus status;
    
    @Column(name = "current_task_id")
    private UUID currentTaskId;
    
    @Column(name = "tasks_completed")
    private Long tasksCompleted;
    
    @Column(name = "tasks_failed")
    private Long tasksFailed;
    
    @Column(name = "last_heartbeat")
    private LocalDateTime lastHeartbeat;
    
    @Column(name = "registered_at")
    private LocalDateTime registeredAt;
    
    @PrePersist
    protected void onCreate() {
        registeredAt = LocalDateTime.now();
        lastHeartbeat = LocalDateTime.now();
    }
}
