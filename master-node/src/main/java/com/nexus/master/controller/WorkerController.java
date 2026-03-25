package com.nexus.master.controller;

import com.nexus.common.dto.TaskDto;
import com.nexus.common.dto.TaskResult;
import com.nexus.common.dto.WorkerHeartbeat;
import com.nexus.master.model.Worker;
import com.nexus.master.service.WorkerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/workers")
@RequiredArgsConstructor
public class WorkerController {

    private final WorkerService workerService;

    @PostMapping("/register")
    public ResponseEntity<Worker> registerWorker() {
        return ResponseEntity.ok(workerService.registerWorker());
    }

    @PostMapping("/heartbeat")
    public ResponseEntity<Void> heartbeat(@RequestBody WorkerHeartbeat heartbeat) {
        workerService.updateHeartbeat(heartbeat);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{workerId}/task")
    public ResponseEntity<TaskDto> getTask(@PathVariable String workerId) {
        TaskDto task = workerService.getTaskForWorker(workerId);
        return task != null ? ResponseEntity.ok(task) : ResponseEntity.noContent().build();
    }

    @PostMapping("/submitResult")
    public ResponseEntity<Void> submitResult(@RequestBody TaskResult result) {
        workerService.submitTaskResult(result);
        return ResponseEntity.ok().build();
    }
}
