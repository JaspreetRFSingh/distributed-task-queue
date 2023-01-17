package com.taskqueue.service;

import com.taskqueue.entity.Task;
import com.taskqueue.entity.TaskStatus;
import com.taskqueue.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class TaskDispatcher {
    private final RabbitTemplate rabbitTemplate;
    private final RedisTemplate<String, String> redisTemplate;
    private final TaskRepository taskRepository;
    
    public void dispatch(Task task) {
        if (task.getScheduledAt() != null && task.getScheduledAt().isAfter(java.time.LocalDateTime.now())) {
            log.info("Scheduling task {} for {}", task.getId(), task.getScheduledAt());
            scheduleTask(task);
            return;
        }
        
        task.setStatus(TaskStatus.QUEUED);
        taskRepository.save(task);
        
        String queueName = task.getQueueName() != null ? task.getQueueName() : "default";
        rabbitTemplate.convertAndSend("task.exchange", "task.queue." + queueName, task);
        
        log.info("Dispatched task {} to queue {}", task.getId(), queueName);
    }
    
    private void scheduleTask(Task task) {
        String key = "scheduled:tasks:" + task.getId();
        redisTemplate.opsForValue().set(key, task.getId().toString());
        redisTemplate.expireAt(key, java.util.Date.from(task.getScheduledAt().atZone(java.time.ZoneId.systemDefault()).toInstant()));
    }
}
