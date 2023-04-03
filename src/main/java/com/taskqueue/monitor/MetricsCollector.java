package com.taskqueue.monitor;

import com.taskqueue.dto.QueueMetrics;
import com.taskqueue.entity.TaskStatus;
import com.taskqueue.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class MetricsCollector {
    private final TaskRepository taskRepository;
    
    public List<QueueMetrics> getQueueMetrics() {
        List<QueueMetrics> metrics = new ArrayList<>();
        
        String[] queues = {"default", "high-priority", "low-priority"};
        for (String queue : queues) {
            QueueMetrics metric = QueueMetrics.builder()
                    .queueName(queue)
                    .pendingTasks(taskRepository.countByStatus(TaskStatus.PENDING))
                    .runningTasks(taskRepository.countByStatus(TaskStatus.RUNNING))
                    .completedTasks(taskRepository.countByStatus(TaskStatus.COMPLETED))
                    .failedTasks(taskRepository.countByStatus(TaskStatus.FAILED))
                    .deadLetterTasks(taskRepository.countByStatus(TaskStatus.DEAD_LETTER))
                    .build();
            metrics.add(metric);
        }
        
        return metrics;
    }
}
