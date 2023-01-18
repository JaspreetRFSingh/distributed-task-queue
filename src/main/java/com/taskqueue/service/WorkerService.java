package com.taskqueue.service;

import com.taskqueue.dto.WorkerRegistrationRequest;
import com.taskqueue.entity.Worker;
import com.taskqueue.entity.WorkerStatus;
import com.taskqueue.exception.WorkerNotFoundException;
import com.taskqueue.repository.WorkerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class WorkerService {
    private final WorkerRepository workerRepository;
    
    @Transactional
    public Worker registerWorker(WorkerRegistrationRequest request) {
        Worker worker = Worker.builder()
                .workerId(request.getWorkerId())
                .hostname(request.getHostname())
                .port(request.getPort())
                .status(WorkerStatus.IDLE)
                .tasksCompleted(0L)
                .tasksFailed(0L)
                .build();
        
        worker = workerRepository.save(worker);
        log.info("Registered worker: {} at {}:{}", worker.getWorkerId(), worker.getHostname(), worker.getPort());
        return worker;
    }
    
    @Transactional
    public void heartbeat(String workerId) {
        Worker worker = workerRepository.findByWorkerId(workerId)
                .orElseThrow(() -> new WorkerNotFoundException("Worker not found: " + workerId));
        
        worker.setLastHeartbeat(LocalDateTime.now());
        if (worker.getStatus() == WorkerStatus.UNHEALTHY) {
            worker.setStatus(WorkerStatus.IDLE);
        }
        
        workerRepository.save(worker);
    }
    
    @Transactional(readOnly = true)
    public List<Worker> getActiveWorkers() {
        return workerRepository.findByStatus(WorkerStatus.ACTIVE);
    }
    
    @Transactional
    public void markWorkerOffline(String workerId) {
        Worker worker = workerRepository.findByWorkerId(workerId)
                .orElseThrow(() -> new WorkerNotFoundException("Worker not found: " + workerId));
        
        worker.setStatus(WorkerStatus.OFFLINE);
        workerRepository.save(worker);
        log.info("Marked worker offline: {}", workerId);
    }
}
