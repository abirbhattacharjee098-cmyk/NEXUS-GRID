package com.nexus.common.dto;

import com.nexus.common.enums.TaskStatus;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskResult {
    private String taskId;
    private String workerId;
    private TaskStatus status;
    private String resultPayload;
}
