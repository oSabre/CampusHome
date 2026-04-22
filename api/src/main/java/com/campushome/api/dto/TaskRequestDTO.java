package com.campushome.api.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskRequestDTO{
    @NotBlank(message = "A descrição da tarefa não pode estar em branco")
    private String description;
}
