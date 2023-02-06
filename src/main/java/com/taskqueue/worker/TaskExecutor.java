package com.taskqueue.worker;

import com.taskqueue.entity.Task;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class TaskExecutor {
    public void execute(Task task) {
        log.info("Executing task: {} of type: {}", task.getId(), task.getTaskType());
        
        switch (task.getTaskType()) {
            case "EMAIL":
                executeEmailTask(task);
                break;
            case "DATA_PROCESSING":
                executeDataProcessingTask(task);
                break;
            case "WEBHOOK":
                executeWebhookTask(task);
                break;
            default:
                log.warn("Unknown task type: {}", task.getTaskType());
        }
    }
    
    private void executeEmailTask(Task task) {
        log.info("Sending email for task: {}", task.getId());
    }
    
    private void executeDataProcessingTask(Task task) {
        log.info("Processing data for task: {}", task.getId());
    }
    
    private void executeWebhookTask(Task task) {
        log.info("Calling webhook for task: {}", task.getId());
    }
}
