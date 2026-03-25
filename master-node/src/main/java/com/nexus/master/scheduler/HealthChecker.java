package com.nexus.master.scheduler;

import com.nexus.common.enums.TaskStatus;
import com.nexus.common.enums.WorkerStatus;
import com.nexus.master.model.Task;
import com.nexus.master.model.Worker;
import com.nexus.master.repository.TaskRepository;
import com.nexus.master.repository.WorkerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class HealthChecker {

    private final WorkerRepository workerRepository;
    private final TaskRepository taskRepository;

    @Value("${nexus.master.health-checker.timeout-seconds}")
    private long timeoutSeconds;

    @Scheduled(fixedRateString = "${nexus.master.health-checker.fixed-rate}")
    @Transactional
    public void performHealthCheck() {
        log.debug("Running Health Checker...");
        
        LocalDateTime timeoutThreshold = LocalDateTime.now().minusSeconds(timeoutSeconds);
        List<Worker> activeWorkers = workerRepository.findByStatus(WorkerStatus.ACTIVE);

        for (Worker worker : activeWorkers) {
            if (worker.getLastHeartbeat().isBefore(timeoutThreshold)) {
                log.warn("Worker {} missed heartbeats for over {}s. Marking as DEAD.", worker.getWorkerId(), timeoutSeconds);
                worker.setStatus(WorkerStatus.DEAD);
                workerRepository.save(worker);
                
                reassignTasks(worker.getWorkerId());
            }
        }
        
        checkAutoScaling();
    }

    private void reassignTasks(String deadWorkerId) {
        List<Task> deadWorkerTasks = taskRepository.findByAssignedWorkerId(deadWorkerId);
        int reassignedCount = 0;
        
        for (Task task : deadWorkerTasks) {
            if (task.getStatus() == TaskStatus.PENDING || task.getStatus() == TaskStatus.RUNNING) {
                task.setAssignedWorkerId(null);
                task.setStatus(TaskStatus.PENDING);
                taskRepository.save(task);
                reassignedCount++;
            }
        }
        
        if (reassignedCount > 0) {
            log.info("Reassigned {} tasks from dead worker {}", reassignedCount, deadWorkerId);
        }
    }
    
    // Simulating Phase 6 AI-based Auto-scaling for now
    private void checkAutoScaling() {
        long pendingTasks = taskRepository.findByStatus(TaskStatus.PENDING).size();
        if (pendingTasks > 5) {
            log.info("[AUTO-SCALER] High load detected ({} pending tasks). Triggering Auto-scaling logic to spin up new worker nodes.", pendingTasks);
        }
    }
}
