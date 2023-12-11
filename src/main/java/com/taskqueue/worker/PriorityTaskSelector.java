package com.taskqueue.worker;

import com.taskqueue.entity.Task;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
public class PriorityTaskSelector {
    public List<Task> selectHighPriorityTasks(List<Task> tasks, int limit) {
        return tasks.stream()
                .filter(t -> t.getPriority() >= 5)
                .sorted(Comparator.comparingInt(Task::getPriority).reversed())
                .limit(limit)
                .collect(Collectors.toList());
    }
}
