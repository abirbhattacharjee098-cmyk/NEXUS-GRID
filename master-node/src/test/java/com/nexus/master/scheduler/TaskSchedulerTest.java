package com.nexus.master.scheduler;

import com.nexus.common.enums.TaskStatus;
import com.nexus.common.enums.WorkerStatus;
import com.nexus.master.model.Task;
import com.nexus.master.model.Worker;
import com.nexus.master.repository.TaskRepository;
import com.nexus.master.repository.WorkerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TaskSchedulerTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private WorkerRepository workerRepository;

    @InjectMocks
    private TaskScheduler taskScheduler;

    @Test
    void testScheduleTasks_AssignsToLowestCpuWorker() {
        Task pendingTask1 = new Task("t1", "j1", null, TaskStatus.PENDING, "payload1");
        Task pendingTask2 = new Task("t2", "j1", null, TaskStatus.PENDING, "payload2");

        Worker w1 = new Worker("w1", WorkerStatus.ACTIVE, 10.0, LocalDateTime.now());
        Worker w2 = new Worker("w2", WorkerStatus.ACTIVE, 5.0, LocalDateTime.now());

        // workerRepository returns lowest CPU first
        List<Worker> activeWorkers = new ArrayList<>(Arrays.asList(w2, w1));

        when(taskRepository.findByStatus(TaskStatus.PENDING)).thenReturn(Arrays.asList(pendingTask1, pendingTask2));
        when(workerRepository.findByStatusOrderByCpuUsageAsc(WorkerStatus.ACTIVE)).thenReturn(activeWorkers);

        taskScheduler.scheduleTasks();

        verify(taskRepository, times(2)).save(any(Task.class));
        assert("w2".equals(pendingTask1.getAssignedWorkerId()));
        assert("w1".equals(pendingTask2.getAssignedWorkerId()));
    }
}
