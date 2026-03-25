package com.nexus.common.dto;

import com.nexus.common.enums.JobType;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JobRequest {
    private JobType jobType;
    private String payload;
}
