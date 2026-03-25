package com.nexus.master.scheduler;

import com.nexus.common.enums.TaskStatus;
import com.nexus.common.enums.WorkerStatus;
import com.nexus.master.model.Task;
import com.nexus.master.model.Worker;
import com.nexus.master.repository.TaskRepository;
import com.nexus.master.repository.WorkerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component("nexusTaskScheduler")
@RequiredArgsConstructor
@Slf4j
public class TaskScheduler {

    private final TaskRepository taskRepository;
    private final WorkerRepository workerRepository;
    private final com.nexus.master.service.AIPredictionService aiPredictionService;

    @Scheduled(fixedRateString = "${nexus.master.scheduler.fixed-rate}")
    @Transactional
    public void scheduleTasks() {
        log.debug("Running Task Scheduler...");
        
        List<Task> pendingTasks = taskRepository.findByStatus(TaskStatus.PENDING);
        if (pendingTasks.isEmpty()) {
            return;
        }

        // Basic Scheduler: Get ACTIVE workers sorted by CPU usage
        List<Worker> activeWorkers = workerRepository.findByStatusOrderByCpuUsageAsc(WorkerStatus.ACTIVE);
        if (activeWorkers.isEmpty()) {
            log.warn("No active workers available to schedule tasks.");
            return;
        }

        // Assign PENDING tasks that have NO assigned worker yet
        for (Task task : pendingTasks) {
            if (task.getAssignedWorkerId() == null) {
                // AI-Based Predictive Scheduling
                Worker optimalWorker = aiPredictionService.predictOptimalWorker(task, activeWorkers);
                if (optimalWorker != null) {
                    task.setAssignedWorkerId(optimalWorker.getWorkerId());
                    taskRepository.save(task);
                    log.info("Assigned Task {} to Worker {} via AI Prediction", task.getTaskId(), optimalWorker.getWorkerId());
                }
            }
        }
    }
}
