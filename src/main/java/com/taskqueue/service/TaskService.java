package com.taskqueue.service;

import com.taskqueue.dto.TaskRequest;
import com.taskqueue.dto.TaskResponse;
import com.taskqueue.entity.Task;
import com.taskqueue.entity.TaskStatus;
import com.taskqueue.exception.TaskNotFoundException;
import com.taskqueue.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class TaskService {
    private final TaskRepository taskRepository;
    private final TaskDispatcher taskDispatcher;
    
    @Transactional
    public TaskResponse createTask(TaskRequest request) {
        Task task = Task.builder()
                .taskType(request.getTaskType())
                .payload(request.getPayload())
                .status(TaskStatus.PENDING)
                .priority(request.getPriority())
                .queueName(request.getQueueName() != null ? request.getQueueName() : "default")
                .maxRetries(request.getMaxRetries())
                .retryCount(0)
                .build();
        
        if (request.getScheduledAt() != null) {
            task.setScheduledAt(LocalDateTime.parse(request.getScheduledAt()));
        }
        
        task = taskRepository.save(task);
        log.info("Created task: {}", task.getId());
        
        taskDispatcher.dispatch(task);
        
        return toResponse(task);
    }
    
    @Transactional(readOnly = true)
    public TaskResponse getTask(UUID taskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException("Task not found: " + taskId));
        return toResponse(task);
    }
    
    @Transactional(readOnly = true)
    public Page<TaskResponse> getTasksByStatus(TaskStatus status, Pageable pageable) {
        return taskRepository.findAll(pageable).map(this::toResponse);
    }
    
    @Transactional(readOnly = true)
    public List<TaskResponse> getDeadLetterTasks(Pageable pageable) {
        return taskRepository.findDeadLetterTasks(pageable).getContent()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }
    
    @Transactional
    public TaskResponse retryTask(UUID taskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException("Task not found: " + taskId));
        
        if (task.getRetryCount() >= task.getMaxRetries()) {
            throw new IllegalStateException("Max retries exceeded for task: " + taskId);
        }
        
        task.setStatus(TaskStatus.PENDING);
        task.setRetryCount(task.getRetryCount() + 1);
        task.setErrorMessage(null);
        task.setWorkerId(null);
        
        task = taskRepository.save(task);
        taskDispatcher.dispatch(task);
        
        log.info("Retrying task: {}, attempt: {}", taskId, task.getRetryCount());
        return toResponse(task);
    }
    
    @Transactional
    public void cancelTask(UUID taskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException("Task not found: " + taskId));
        
        if (task.getStatus() == TaskStatus.RUNNING) {
            throw new IllegalStateException("Cannot cancel running task: " + taskId);
        }
        
        task.setStatus(TaskStatus.CANCELLED);
        taskRepository.save(task);
        log.info("Cancelled task: {}", taskId);
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
                .scheduledAt(task.getScheduledAt())
                .startedAt(task.getStartedAt())
                .completedAt(task.getCompletedAt())
                .errorMessage(task.getErrorMessage())
                .workerId(task.getWorkerId())
                .build();
    }
}
