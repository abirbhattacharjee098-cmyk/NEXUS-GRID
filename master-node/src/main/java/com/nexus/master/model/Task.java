package com.nexus.master.model;

import com.nexus.common.enums.TaskStatus;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "tasks")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Task {
    @Id
    private String taskId;
    
    private String jobId;
    private String assignedWorkerId;
    
    @Enumerated(EnumType.STRING)
    private TaskStatus status;
    
    @Column(columnDefinition = "TEXT")
    private String payload;
}
