package com.taskqueue.scheduler;

import com.taskqueue.entity.Task;
import com.taskqueue.entity.TaskStatus;
import com.taskqueue.repository.TaskRepository;
import com.taskqueue.service.TaskDispatcher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class TaskScheduler {
    private final TaskRepository taskRepository;
    private final TaskDispatcher taskDispatcher;
    
    @Scheduled(cron = "${taskqueue.scheduler.cron:0 */1 * * * *}")
    public void processScheduledTasks() {
        List<Task> dueTasks = taskRepository.findDueTasks(TaskStatus.PENDING, LocalDateTime.now());
        
        log.info("Found {} scheduled tasks to process", dueTasks.size());
        
        for (Task task : dueTasks) {
            log.info("Dispatching scheduled task: {}", task.getId());
            taskDispatcher.dispatch(task);
        }
    }
}
