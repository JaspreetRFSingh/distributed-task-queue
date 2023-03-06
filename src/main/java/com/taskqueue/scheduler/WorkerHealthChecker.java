package com.taskqueue.scheduler;

import com.taskqueue.entity.Worker;
import com.taskqueue.entity.WorkerStatus;
import com.taskqueue.repository.WorkerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class WorkerHealthChecker {
    private final WorkerRepository workerRepository;
    
    @Scheduled(fixedRate = 30000)
    public void checkWorkerHealth() {
        LocalDateTime threshold = LocalDateTime.now().minusMinutes(5);
        List<Worker> staleWorkers = workerRepository.findStaleWorkers(threshold);
        
        for (Worker worker : staleWorkers) {
            if (worker.getStatus() != WorkerStatus.OFFLINE) {
                log.warn("Marking worker as unhealthy: {}", worker.getWorkerId());
                worker.setStatus(WorkerStatus.UNHEALTHY);
                workerRepository.save(worker);
            }
        }
    }
}
