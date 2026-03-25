package com.nexus.master.service;

import com.nexus.common.dto.TaskDto;
import com.nexus.common.dto.TaskResult;
import com.nexus.common.dto.WorkerHeartbeat;
import com.nexus.common.enums.TaskStatus;
import com.nexus.common.enums.WorkerStatus;
import com.nexus.master.model.Task;
import com.nexus.master.model.Worker;
import com.nexus.master.repository.TaskRepository;
import com.nexus.master.repository.WorkerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class WorkerService {

    private final WorkerRepository workerRepository;
    private final TaskRepository taskRepository;

    @Transactional
    public Worker registerWorker() {
        Worker worker = new Worker();
        worker.setWorkerId(UUID.randomUUID().toString());
        worker.setStatus(WorkerStatus.ACTIVE);
        worker.setCpuUsage(0.0);
        worker.setLastHeartbeat(LocalDateTime.now());
        
        worker = workerRepository.save(worker);
        log.info("Registered new worker: {}", worker.getWorkerId());
        return worker;
    }

    @Transactional
    public void updateHeartbeat(WorkerHeartbeat heartbeat) {
        workerRepository.findById(heartbeat.getWorkerId()).ifPresent(worker -> {
            worker.setCpuUsage(heartbeat.getCpuUsage());
            worker.setLastHeartbeat(LocalDateTime.now());
            if (worker.getStatus() == WorkerStatus.DEAD) {
                worker.setStatus(WorkerStatus.ACTIVE);
                log.info("Worker {} recovered from DEAD state", worker.getWorkerId());
            }
            workerRepository.save(worker);
            log.debug("Heartbeat updated for worker: {}", worker.getWorkerId());
        });
    }

    @Transactional
    public TaskDto getTaskForWorker(String workerId) {
        // Find tasks assigned to this worker that are PENDING
        List<Task> assignedTasks = taskRepository.findByAssignedWorkerId(workerId);
        Task pendingTask = assignedTasks.stream()
                .filter(t -> t.getStatus() == TaskStatus.PENDING)
                .findFirst()
                .orElse(null);

        if (pendingTask != null) {
            pendingTask.setStatus(TaskStatus.RUNNING);
            taskRepository.save(pendingTask);
            log.info("Task {} started by worker {}", pendingTask.getTaskId(), workerId);
            return new TaskDto(pendingTask.getTaskId(), pendingTask.getJobId(), pendingTask.getPayload(), TaskStatus.RUNNING);
        }
        return null;
    }

    @Transactional
    public void submitTaskResult(TaskResult result) {
        taskRepository.findById(result.getTaskId()).ifPresent(task -> {
            task.setStatus(result.getStatus());
            if (result.getResultPayload() != null && !result.getResultPayload().isEmpty()) {
                task.setPayload(result.getResultPayload()); // Save JSON result back into payload field
            }
            taskRepository.save(task);
            log.info("Task {} completed with status {} by worker {}", 
                task.getTaskId(), result.getStatus(), result.getWorkerId());
            
            checkAndAggregateJob(task.getJobId());
        });
    }

    private void checkAndAggregateJob(String jobId) {
        List<Task> allTasks = taskRepository.findAll().stream()
                .filter(t -> t.getJobId().equals(jobId))
                .toList(); // Using java 16+ toList
                
        boolean allFinished = allTasks.stream().allMatch(t -> t.getStatus() == TaskStatus.COMPLETED || t.getStatus() == TaskStatus.FAILED);
        
        if (allFinished && allTasks.size() > 0 && allTasks.get(0).getPayload() != null && allTasks.get(0).getPayload().startsWith("{")) {
            log.info("============== JOB {} COMPLETED ==============", jobId);
            try {
                com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
                int totalMessi = 0, totalRonaldo = 0, totalGoals = 0, totalAssists = 0;
                
                log.info(String.format("%-10s | %-10s | %-16s | %-16s", "Messi", "Ronaldo", "Goal Mentions", "Assist Mentions"));
                log.info("---------------------------------------------------------------");
                
                for (Task t : allTasks) {
                    if (t.getPayload() != null && t.getPayload().startsWith("{") && !t.getPayload().contains("error")) {
                        com.fasterxml.jackson.databind.JsonNode node = mapper.readTree(t.getPayload());
                        totalMessi += node.get("messi").asInt();
                        totalRonaldo += node.get("ronaldo").asInt();
                        totalGoals += node.get("goals").asInt();
                        totalAssists += node.get("assists").asInt();
                    }
                }
                
                log.info(String.format("%-10d | %-10d | %-16d | %-16d", totalMessi, totalRonaldo, totalGoals, totalAssists));
                
                if (totalMessi > totalRonaldo) {
                    log.info(">>> VERDICT: Messi dominates the conversation across these 10 pages! <<<");
                } else if (totalRonaldo > totalMessi) {
                    log.info(">>> VERDICT: Ronaldo dominates the conversation across these 10 pages! <<<");
                } else {
                    log.info(">>> VERDICT: It's a dead tie! <<<");
                }
                log.info("==============================================================");
            } catch (Exception e) {
                 log.error("Failed to aggregate results: {}", e.getMessage());
            }
        }
    }
}
