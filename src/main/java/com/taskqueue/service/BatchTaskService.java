package com.taskqueue.service;

import com.taskqueue.dto.TaskRequest;
import com.taskqueue.dto.TaskResponse;
import com.taskqueue.entity.Task;
import com.taskqueue.entity.TaskStatus;
import com.taskqueue.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class BatchTaskService {
    private final TaskRepository taskRepository;
    private final TaskDispatcher taskDispatcher;
    
    @Transactional
    public List<TaskResponse> createBatchTasks(List<TaskRequest> requests) {
        log.info("Creating {} tasks in batch", requests.size());
        
        List<Task> tasks = requests.stream()
                .map(req -> Task.builder()
                        .taskType(req.getTaskType())
                        .payload(req.getPayload())
                        .status(TaskStatus.PENDING)
                        .priority(req.getPriority())
                        .queueName(req.getQueueName() != null ? req.getQueueName() : "default")
                        .maxRetries(req.getMaxRetries())
                        .retryCount(0)
                        .build())
                .collect(Collectors.toList());
        
        tasks = taskRepository.saveAll(tasks);
        
        tasks.forEach(taskDispatcher::dispatch);
        
        return tasks.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }
    
    private TaskResponse toResponse(Task task) {
        return TaskResponse.builder()
                .id(task.getId())
                .taskType(task.getTaskType())
                .status(task.getStatus())
                .priority(task.getPriority())
                .queueName(task.getQueueName())
                .retryCount(task.getRetryCount())
                .maxRetries(task.getMaxRetries())
                .createdAt(task.getCreatedAt())
                .build();
    }
}
