package com.taskqueue.controller;

import com.taskqueue.dto.TaskRequest;
import com.taskqueue.dto.TaskResponse;
import com.taskqueue.service.BatchTaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tasks/batch")
@RequiredArgsConstructor
public class BatchTaskController {
    private final BatchTaskService batchTaskService;
    
    @PostMapping
    public ResponseEntity<List<TaskResponse>> createBatchTasks(@RequestBody List<TaskRequest> requests) {
        return ResponseEntity.status(HttpStatus.CREATED).body(batchTaskService.createBatchTasks(requests));
    }
}
