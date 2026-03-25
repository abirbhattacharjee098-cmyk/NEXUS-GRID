package com.nexus.master.model;

import com.nexus.common.enums.WorkerStatus;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "workers")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Worker {
    @Id
    private String workerId;
    
    @Enumerated(EnumType.STRING)
    private WorkerStatus status;
    
    private Double cpuUsage;
    private LocalDateTime lastHeartbeat;
}
