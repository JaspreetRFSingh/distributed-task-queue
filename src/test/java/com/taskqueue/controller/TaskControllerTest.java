package com.taskqueue.controller;

import com.taskqueue.dto.TaskRequest;
import com.taskqueue.dto.TaskResponse;
import com.taskqueue.entity.TaskStatus;
import com.taskqueue.service.TaskService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.bean.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TaskController.class)
class TaskControllerTest {
    @Autowired
    private MockMvc mockMvc;
    
    @MockBean
    private TaskService taskService;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    @Test
    void createTask_shouldReturnCreatedTask() throws Exception {
        TaskRequest request = TaskRequest.builder()
                .taskType("EMAIL")
                .payload("{\"to\":\"test@example.com\"}")
                .build();
        
        TaskResponse response = TaskResponse.builder()
                .id(UUID.randomUUID())
                .taskType("EMAIL")
                .status(TaskStatus.PENDING)
                .build();
        
        when(taskService.createTask(any())).thenReturn(response);
        
        mockMvc.perform(post("/api/v1/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.taskType").value("EMAIL"));
    }
}
