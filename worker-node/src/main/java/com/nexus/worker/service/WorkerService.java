package com.nexus.worker.service;

import com.nexus.common.dto.TaskDto;
import com.nexus.common.dto.TaskResult;
import com.nexus.common.dto.WorkerHeartbeat;
import com.nexus.common.enums.TaskStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import jakarta.annotation.PostConstruct;

import java.util.Map;

@Service
@Slf4j
public class WorkerService {

    private final WebClient webClient;
    private String workerId;
    
    @Value("${nexus.worker.master-url}")
    private String masterUrl;

    public WorkerService(WebClient.Builder webClientBuilder, @Value("${nexus.worker.master-url}") String masterUrl) {
        this.webClient = webClientBuilder.baseUrl(masterUrl).build();
        this.masterUrl = masterUrl;
    }

    @PostConstruct
    public void registerToMaster() {
        log.info("Registering worker to master at {}", masterUrl);
        try {
            Map<String, Object> response = webClient.post()
                    .uri("/api/workers/register")
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
                    .block();
            
            if (response != null && response.containsKey("workerId")) {
                this.workerId = (String) response.get("workerId");
                log.info("Successfully registered with Worker ID: {}", workerId);
            }
        } catch (Exception e) {
            log.error("Failed to register worker. Is Master Node running? Error: {}", e.getMessage());
        }
    }

    @Scheduled(fixedRateString = "${nexus.worker.heartbeat.fixed-rate}")
    public void sendHeartbeat() {
        if (workerId == null) {
            registerToMaster(); // Retry registration if missing
            return;
        }
        
        // Mock CPU usage calculation
        double cpuUsage = Math.random() * 100;
        WorkerHeartbeat heartbeat = new WorkerHeartbeat(workerId, cpuUsage);
        
        try {
            webClient.post()
                    .uri("/api/workers/heartbeat")
                    .bodyValue(heartbeat)
                    .retrieve()
                    .bodyToMono(Void.class)
                    .block();
            log.debug("Heartbeat sent: CPU {}%", String.format("%.2f", cpuUsage));
        } catch (Exception e) {
            log.warn("Failed to send heartbeat: {}", e.getMessage());
        }
    }

    @Scheduled(fixedRateString = "${nexus.worker.task-poller.fixed-rate}")
    public void pollForTasks() {
        if (workerId == null) return;
        
        try {
            TaskDto task = webClient.get()
                    .uri("/api/workers/" + workerId + "/task")
                    .retrieve()
                    .bodyToMono(TaskDto.class)
                    .block();
            
            if (task != null && task.getTaskId() != null) {
                log.info("Received Task: {} for Job: {}", task.getTaskId(), task.getJobId());
                executeTask(task);
            }
        } catch (Exception e) {
            // Ignore 204 No Content exceptions or connection refused occasionally
            log.debug("No tasks available or master unreachable");
        }
    }

    private void executeTask(TaskDto task) {
        log.info("Executing Task: {}", task.getTaskId());
        String resultPayload = "";
        
        if (task.getPayload() != null && task.getPayload().startsWith("http")) { // WEB_SCRAPING
            try {
                log.info("Scraping URL: {}", task.getPayload());
                org.jsoup.nodes.Document doc = org.jsoup.Jsoup.connect(task.getPayload())
                     .userAgent("Mozilla/5.0")
                     .timeout(10000)
                     .get();
                     
                String text = doc.text().toLowerCase();
                
                // Count occurrences for analysis
                int messiMentions = text.split("messi").length - 1;
                int ronaldoMentions = text.split("ronaldo").length - 1;
                int goalMentions = text.split("goal").length - 1;
                int assistMentions = text.split("assist").length - 1;
                
                resultPayload = String.format("{\"url\": \"%s\", \"messi\": %d, \"ronaldo\": %d, \"goals\": %d, \"assists\": %d}", 
                        task.getPayload(), messiMentions, ronaldoMentions, goalMentions, assistMentions);
                
                log.info("Scraping complete for {}. Messi: {}, Ronaldo: {}", task.getPayload(), messiMentions, ronaldoMentions);
            } catch (Exception e) {
                log.error("Failed to scrape URL {}. Error: {}", task.getPayload(), e.getMessage());
                resultPayload = "{\"error\": \"Scraping failed\"}";
            }
        } else {
            // Mock task execution delay
            try {
                Thread.sleep(2000); 
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            resultPayload = "Processed: " + task.getPayload();
        }
        
        log.info("Task Execution Completed: {}", task.getTaskId());

        TaskResult result = new TaskResult(task.getTaskId(), workerId, TaskStatus.COMPLETED, resultPayload);
        
        try {
            webClient.post()
                    .uri("/api/workers/submitResult")
                    .bodyValue(result)
                    .retrieve()
                    .bodyToMono(Void.class)
                    .block();
            log.info("Submitted Result for Task: {}", task.getTaskId());
        } catch (Exception e) {
            log.error("Failed to submit result for Task: {}", task.getTaskId());
        }
    }
}
