package com.taskqueue.service;

import com.taskqueue.dto.TaskRequest;
import com.taskqueue.dto.TaskResponse;
import com.taskqueue.entity.Task;
import com.taskqueue.entity.TaskStatus;
import com.taskqueue.exception.TaskNotFoundException;
import com.taskqueue.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
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
class TaskServiceTest {
    @Mock
    private TaskRepository taskRepository;
    
    @Mock
    private TaskDispatcher taskDispatcher;
    
    @InjectMocks
    private TaskService taskService;
    
    private TaskRequest taskRequest;
    
    @BeforeEach
    void setUp() {
        taskRequest = TaskRequest.builder()
                .taskType("EMAIL")
                .payload("{\"to\":\"test@example.com\"}")
                .priority(5)
                .queueName("default")
                .maxRetries(3)
                .build();
    }
    
    @Test
    void createTask_shouldReturnTaskResponse() {
        Task task = Task.builder()
                .id(UUID.randomUUID())
                .taskType("EMAIL")
                .status(TaskStatus.PENDING)
                .build();
        
        when(taskRepository.save(any())).thenReturn(task);
        
        TaskResponse response = taskService.createTask(taskRequest);
        
        assertNotNull(response);
        assertEquals("EMAIL", response.getTaskType());
        verify(taskDispatcher).dispatch(any());
    }
    
    @Test
    void getTask_shouldReturnTaskResponse() {
        UUID taskId = UUID.randomUUID();
        Task task = Task.builder()
                .id(taskId)
                .taskType("EMAIL")
                .status(TaskStatus.PENDING)
                .build();
        
        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));
        
        TaskResponse response = taskService.getTask(taskId);
        
        assertNotNull(response);
        assertEquals(taskId, response.getId());
    }
    
    @Test
    void getTask_shouldThrowExceptionWhenNotFound() {
        UUID taskId = UUID.randomUUID();
        when(taskRepository.findById(taskId)).thenReturn(Optional.empty());
        
        assertThrows(TaskNotFoundException.class, () -> taskService.getTask(taskId));
    }
}
