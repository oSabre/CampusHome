package com.carpooling.api.dto;

import com.carpooling.api.enums.TaskType;

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