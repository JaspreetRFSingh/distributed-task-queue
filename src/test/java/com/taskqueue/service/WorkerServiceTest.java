package com.taskqueue.service;

import com.taskqueue.dto.WorkerRegistrationRequest;
import com.taskqueue.entity.Worker;
import com.taskqueue.entity.WorkerStatus;
import com.taskqueue.exception.WorkerNotFoundException;
import com.taskqueue.repository.WorkerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WorkerServiceTest {
    @Mock
    private WorkerRepository workerRepository;
    
    @InjectMocks
    private WorkerService workerService;
    
    @Test
    void registerWorker_shouldReturnWorker() {
        WorkerRegistrationRequest request = WorkerRegistrationRequest.builder()
                .workerId("worker-1")
                .hostname("localhost")
                .port(8081)
                .build();
        
        Worker worker = Worker.builder()
                .workerId("worker-1")
                .status(WorkerStatus.IDLE)
                .build();
        
        when(workerRepository.save(any())).thenReturn(worker);
        
        Worker result = workerService.registerWorker(request);
        
        assertNotNull(result);
        assertEquals("worker-1", result.getWorkerId());
    }
    
    @Test
    void heartbeat_shouldUpdateLastHeartbeat() {
        String workerId = "worker-1";
        Worker worker = Worker.builder().workerId(workerId).build();
        
        when(workerRepository.findByWorkerId(workerId)).thenReturn(Optional.of(worker));
        
        workerService.heartbeat(workerId);
        
        verify(workerRepository).save(worker);
        assertNotNull(worker.getLastHeartbeat());
    }
    
    @Test
    void heartbeat_shouldThrowExceptionWhenWorkerNotFound() {
        when(workerRepository.findByWorkerId(any())).thenReturn(Optional.empty());
        
        assertThrows(WorkerNotFoundException.class, () -> workerService.heartbeat("invalid"));
    }
}
