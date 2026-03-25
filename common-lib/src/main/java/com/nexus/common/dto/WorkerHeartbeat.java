package com.nexus.common.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WorkerHeartbeat {
    private String workerId;
    private double cpuUsage;
}
