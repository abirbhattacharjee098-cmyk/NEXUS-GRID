package com.nexus.common.dto;

import com.nexus.common.enums.TaskStatus;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskDto {
    private String taskId;
    private String jobId;
    private String payload;
    private TaskStatus status;
}
