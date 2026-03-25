package com.nexus.master.service;

import com.nexus.master.model.Task;
import com.nexus.master.model.Worker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class AIPredictionService {

    /**
     * Simulates an AI/ML model predicting the best worker for a given task.
     * Uses heuristics like the payload complexity and current worker CPU usage
     * to approximate "historical worker performance" and "predictive task allocation".
     */
    public Worker predictOptimalWorker(Task task, List<Worker> activeWorkers) {
        if (activeWorkers.isEmpty()) {
            return null;
        }

        log.debug("[AI-SCHEDULER] Predicting optimal worker for Task: {}", task.getTaskId());
        
        // Extract basic features (e.g. payload length)
        int payloadComplexity = (task.getPayload() != null) ? task.getPayload().length() : 0;
        
        Worker bestWorker = activeWorkers.get(0);
        double bestScore = Double.MAX_VALUE;

        for (Worker worker : activeWorkers) {
            // Heuristic scoring: lower is better. 
            // - Higher CPU heavily penalizes the score
            // - A large payload on a high CPU worker is very bad
            double cpuFactor = worker.getCpuUsage() == null ? 0.0 : worker.getCpuUsage();
            double predictedExecutionTime = (cpuFactor * 1.5) + (payloadComplexity * 0.1);
            
            log.trace("[AI-SCHEDULER] Worker {} prediction score: {}", worker.getWorkerId(), predictedExecutionTime);
            
            if (predictedExecutionTime < bestScore) {
                bestScore = predictedExecutionTime;
                bestWorker = worker;
            }
        }
        
        log.info("[AI-SCHEDULER] Selected Worker {} with predicted optimal score {}", bestWorker.getWorkerId(), String.format("%.2f", bestScore));
        
        return bestWorker;
    }
}
