package com.expert.api.dto;

import com.expert.api.enums.TaskType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskRequestDTO {
    @NotBlank(message = "A descrição da tarefa não pode estar em branco")
    private String description;

    @NotNull(message = "O tipo da tarefa é obrigatório")
    private TaskType taskType;
}