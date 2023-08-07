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
public class DeadLetterProcessor {
    private final TaskRepository taskRepository;
    
    @RabbitListener(queues = "${taskqueue.dead-letter.queue:task.queue.dead-letter}")
    public void processDeadLetter(Task task) {
        log.error("Processing dead letter task: {}", task.getId());
        
        task.setStatus(TaskStatus.DEAD_LETTER);
        taskRepository.save(task);
    }
}
