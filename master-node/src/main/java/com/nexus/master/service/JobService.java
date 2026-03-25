package com.nexus.master.service;

import com.nexus.common.dto.JobRequest;
import com.nexus.common.enums.JobStatus;
import com.nexus.common.enums.TaskStatus;
import com.nexus.master.model.Job;
import com.nexus.master.model.Task;
import com.nexus.master.repository.JobRepository;
import com.nexus.master.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class JobService {
    
    private final JobRepository jobRepository;
    private final TaskRepository taskRepository;

    @Transactional
    public Job submitJob(JobRequest request) {
        String jobId = UUID.randomUUID().toString();
        
        Job job = new Job();
        job.setJobId(jobId);
        job.setJobType(request.getJobType());
        job.setStatus(JobStatus.PENDING);
        job.setCreatedAt(LocalDateTime.now());
        
        job = jobRepository.save(job);
        
        log.info("Created Job: {}", jobId);
        
        // Split job into tasks
        splitJobIntoTasks(job, request.getPayload());
        
        return job;
    }
    
    private void splitJobIntoTasks(Job job, String payload) {
        if (job.getJobType() == com.nexus.common.enums.JobType.WEB_SCRAPING) {
            String[] urls = {
                "https://en.wikipedia.org/wiki/Lionel_Messi",
                "https://en.wikipedia.org/wiki/Cristiano_Ronaldo",
                "https://en.wikipedia.org/wiki/El_Cl%C3%A1sico",
                "https://en.wikipedia.org/wiki/La_Liga",
                "https://en.wikipedia.org/wiki/UEFA_Champions_League",
                "https://en.wikipedia.org/wiki/Ballon_d%27Or",
                "https://en.wikipedia.org/wiki/FIFA_World_Cup",
                "https://en.wikipedia.org/wiki/FC_Barcelona",
                "https://en.wikipedia.org/wiki/Real_Madrid_CF",
                "https://en.wikipedia.org/wiki/List_of_men%27s_footballers_with_50_or_more_international_goals"
            };
            for (String url : urls) {
                Task task = new Task();
                task.setTaskId(UUID.randomUUID().toString());
                task.setJobId(job.getJobId());
                task.setStatus(TaskStatus.PENDING);
                task.setPayload(url);
                taskRepository.save(task);
                log.info("Created Web Scraping Task: {} for URL: {}", task.getTaskId(), url);
            }
        } else {
            // Simplified task splitting logic for demonstration
            int numberOfTasks = 3; // Hardcoded split into 3 tasks for this phase
            for (int i = 0; i < numberOfTasks; i++) {
                Task task = new Task();
                task.setTaskId(UUID.randomUUID().toString());
                task.setJobId(job.getJobId());
                task.setStatus(TaskStatus.PENDING);
                task.setPayload(payload + " [Part " + (i + 1) + "]");
                taskRepository.save(task);
                log.info("Created Task: {} for Job: {}", task.getTaskId(), job.getJobId());
            }
        }
    }
}
