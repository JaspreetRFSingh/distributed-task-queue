package com.taskqueue.worker;

import com.taskqueue.entity.Task;
import com.taskqueue.entity.TaskStatus;
import com.taskqueue.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class TaskWorker {
    private final TaskRepository taskRepository;
    private final TaskExecutor taskExecutor;
    
    @RabbitListener(queues = "${taskqueue.queue.name:task.queue.default}")
    public void processTask(Task task) {
        log.info("Processing task: {}", task.getId());
        
        try {
            task.setStatus(TaskStatus.RUNNING);
            task.setStartedAt(java.time.LocalDateTime.now());
            taskRepository.save(task);
            
            taskExecutor.execute(task);
            
            task.setStatus(TaskStatus.COMPLETED);
            task.setCompletedAt(java.time.LocalDateTime.now());
            taskRepository.save(task);
            
            log.info("Task completed: {}", task.getId());
        } catch (Exception e) {
            log.error("Task failed: {}", task.getId(), e);
            handleTaskFailure(task, e);
        }
    }
    
    private void handleTaskFailure(Task task, Exception e) {
        task.setRetryCount(task.getRetryCount() + 1);
        task.setErrorMessage(e.getMessage());
        
        if (task.getRetryCount() >= task.getMaxRetries()) {
            task.setStatus(TaskStatus.DEAD_LETTER);
            log.warn("Task moved to dead letter queue: {}", task.getId());
        } else {
            task.setStatus(TaskStatus.RETRYING);
            log.info("Scheduling retry for task: {}, attempt: {}", task.getId(), task.getRetryCount());
        }
        
        taskRepository.save(task);
    }
}
