package com.expert.api.dto;

import com.expert.api.enums.TaskType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskResponseDTO {
    private Long id;
    private String description;
    private boolean completed;
    private TaskType taskType;
}