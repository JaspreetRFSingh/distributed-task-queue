package com.taskqueue.controller;

import com.taskqueue.dto.WorkerRegistrationRequest;
import com.taskqueue.entity.Worker;
import com.taskqueue.service.WorkerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/workers")
@RequiredArgsConstructor
public class WorkerController {
    private final WorkerService workerService;
    
    @PostMapping
    public ResponseEntity<Worker> registerWorker(@RequestBody WorkerRegistrationRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(workerService.registerWorker(request));
    }
    
    @PostMapping("/{workerId}/heartbeat")
    public ResponseEntity<Void> heartbeat(@PathVariable String workerId) {
        workerService.heartbeat(workerId);
        return ResponseEntity.ok().build();
    }
    
    @GetMapping
    public ResponseEntity<List<Worker>> getActiveWorkers() {
        return ResponseEntity.ok(workerService.getActiveWorkers());
    }
    
    @DeleteMapping("/{workerId}")
    public ResponseEntity<Void> markOffline(@PathVariable String workerId) {
        workerService.markWorkerOffline(workerId);
        return ResponseEntity.noContent().build();
    }
}
