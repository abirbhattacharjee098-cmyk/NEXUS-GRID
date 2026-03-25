package com.nexus.master.repository;

import com.nexus.master.model.Task;
import com.nexus.common.enums.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, String> {
    List<Task> findByAssignedWorkerId(String workerId);
    List<Task> findByStatus(TaskStatus status);
}
