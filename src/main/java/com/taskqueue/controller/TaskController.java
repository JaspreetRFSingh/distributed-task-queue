package com.taskqueue.controller;

import com.taskqueue.dto.QueueMetrics;
import com.taskqueue.dto.TaskRequest;
import com.taskqueue.dto.TaskResponse;
import com.taskqueue.entity.TaskStatus;
import com.taskqueue.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/tasks")
@RequiredArgsConstructor
public class TaskController {
    private final TaskService taskService;
    
    @PostMapping
    public ResponseEntity<TaskResponse> createTask(@RequestBody TaskRequest request) {
        TaskResponse response = taskService.createTask(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    @GetMapping("/{taskId}")
    public ResponseEntity<TaskResponse> getTask(@PathVariable UUID taskId) {
        return ResponseEntity.ok(taskService.getTask(taskId));
    }
    
    @GetMapping
    public ResponseEntity<Page<TaskResponse>> getTasks(
            @RequestParam(required = false, defaultValue = "PENDING") TaskStatus status,
            Pageable pageable) {
        return ResponseEntity.ok(taskService.getTasksByStatus(status, pageable));
    }
    
    @GetMapping("/dead-letter")
    public ResponseEntity<List<TaskResponse>> getDeadLetterTasks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Pageable pageable = org.springframework.data.domain.PageRequest.of(page, size);
        return ResponseEntity.ok(taskService.getDeadLetterTasks(pageable));
    }
    
    @PostMapping("/{taskId}/retry")
    public ResponseEntity<TaskResponse> retryTask(@PathVariable UUID taskId) {
        return ResponseEntity.ok(taskService.retryTask(taskId));
    }
    
    @DeleteMapping("/{taskId}")
    public ResponseEntity<Void> cancelTask(@PathVariable UUID taskId) {
        taskService.cancelTask(taskId);
        return ResponseEntity.noContent().build();
    }
}
