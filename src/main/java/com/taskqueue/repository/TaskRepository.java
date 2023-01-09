package com.taskqueue.repository;

import com.taskqueue.entity.Task;
import com.taskqueue.entity.TaskStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface TaskRepository extends JpaRepository<Task, UUID> {
    List<Task> findByStatusAndQueueName(TaskStatus status, String queueName);
    
    List<Task> findByStatusOrderByPriorityDesc(TaskStatus status);
    
    @Query("SELECT t FROM Task t WHERE t.status = :status AND t.scheduledAt <= :now ORDER BY t.priority DESC")
    List<Task> findDueTasks(@Param("status") TaskStatus status, @Param("now") LocalDateTime now);
    
    @Query("SELECT t FROM Task t WHERE t.workerId = :workerId AND t.status = :status")
    List<Task> findTasksByWorkerIdAndStatus(@Param("workerId") String workerId, @Param("status") TaskStatus status);
    
    @Query("SELECT COUNT(t) FROM Task t WHERE t.status = :status")
    Long countByStatus(@Param("status") TaskStatus status);
    
    @Query("SELECT t FROM Task t WHERE t.status = 'DEAD_LETTER' ORDER BY t.updatedAt DESC")
    Page<Task> findDeadLetterTasks(Pageable pageable);
    
    @Query("SELECT t FROM Task t WHERE t.createdAt BETWEEN :start AND :end")
    List<Task> findTasksByDateRange(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);
}
