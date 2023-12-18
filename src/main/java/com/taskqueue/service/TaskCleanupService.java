package com.taskqueue.service;

import com.taskqueue.entity.TaskStatus;
import com.taskqueue.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class TaskCleanupService {
    private final TaskRepository taskRepository;
    
    @Scheduled(cron = "0 0 2 * * *")
    public void cleanupOldTasks() {
        log.info("Running task cleanup job");
        // Implementation for archiving old completed tasks
    }
}
