package com.nexus.master.repository;

import com.nexus.master.model.Worker;
import com.nexus.common.enums.WorkerStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WorkerRepository extends JpaRepository<Worker, String> {
    List<Worker> findByStatusOrderByCpuUsageAsc(WorkerStatus status);
    List<Worker> findByStatus(WorkerStatus status);
}
