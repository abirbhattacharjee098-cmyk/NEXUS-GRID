package com.nexus.master.scheduler;

import com.nexus.common.enums.TaskStatus;
import com.nexus.common.enums.WorkerStatus;
import com.nexus.master.model.Task;
import com.nexus.master.model.Worker;
import com.nexus.master.repository.TaskRepository;
import com.nexus.master.repository.WorkerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class HealthCheckerTest {

    @Mock
    private WorkerRepository workerRepository;

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private HealthChecker healthChecker;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(healthChecker, "timeoutSeconds", 10);
    }

    @Test
    void testPerformHealthCheck_MarksWorkerDeadAndReassignsTasks() {
        Worker deadWorker = new Worker("w1", WorkerStatus.ACTIVE, 10.0, LocalDateTime.now().minusSeconds(20));
        Task assignedTask = new Task("t1", "j1", "w1", TaskStatus.PENDING, "payload1");

        when(workerRepository.findByStatus(WorkerStatus.ACTIVE)).thenReturn(Collections.singletonList(deadWorker));
        when(taskRepository.findByAssignedWorkerId("w1")).thenReturn(Collections.singletonList(assignedTask));
        when(taskRepository.findByStatus(TaskStatus.PENDING)).thenReturn(Collections.emptyList());

        healthChecker.performHealthCheck();

        verify(workerRepository).save(deadWorker);
        assert(deadWorker.getStatus() == WorkerStatus.DEAD);

        verify(taskRepository).save(assignedTask);
        assert(assignedTask.getAssignedWorkerId() == null);
        assert(assignedTask.getStatus() == TaskStatus.PENDING);
    }
}
