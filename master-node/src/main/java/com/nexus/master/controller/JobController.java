package com.nexus.master.controller;

import com.nexus.common.dto.JobRequest;
import com.nexus.master.model.Job;
import com.nexus.master.service.JobService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/jobs")
@RequiredArgsConstructor
public class JobController {

    private final JobService jobService;

    @PostMapping("/submitJob")
    public ResponseEntity<Job> submitJob(@RequestBody JobRequest request) {
        Job job = jobService.submitJob(request);
        return ResponseEntity.ok(job);
    }
}
