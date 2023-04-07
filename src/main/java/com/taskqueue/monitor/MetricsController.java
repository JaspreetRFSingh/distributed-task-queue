package com.taskqueue.monitor;

import com.taskqueue.dto.QueueMetrics;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/metrics")
@RequiredArgsConstructor
public class MetricsController {
    private final MetricsCollector metricsCollector;
    
    @GetMapping
    public ResponseEntity<List<QueueMetrics>> getMetrics() {
        return ResponseEntity.ok(metricsCollector.getQueueMetrics());
    }
}
