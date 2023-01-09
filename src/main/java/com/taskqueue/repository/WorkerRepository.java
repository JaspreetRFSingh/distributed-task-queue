package com.taskqueue.repository;

import com.taskqueue.entity.Worker;
import com.taskqueue.entity.WorkerStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface WorkerRepository extends JpaRepository<Worker, UUID> {
    Optional<Worker> findByWorkerId(String workerId);
    
    List<Worker> findByStatus(WorkerStatus status);
    
    @Query("SELECT w FROM Worker w WHERE w.lastHeartbeat < :threshold")
    List<Worker> findStaleWorkers(@Param("threshold") LocalDateTime threshold);
    
    @Query("SELECT COUNT(w) FROM Worker w WHERE w.status = :status")
    Long countActiveWorkers(@Param("status") WorkerStatus status);
}
