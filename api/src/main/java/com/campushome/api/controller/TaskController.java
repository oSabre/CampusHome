package com.campushome.api.controller;

import com.campushome.api.dto.TaskRequestDTO;
import com.campushome.api.dto.TaskResponseDTO;
import com.campushome.api.service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tasks")
@RequiredArgsConstructor
public class TaskController {
    private final TaskService taskService;

    @PostMapping("/advertisement/{adId}")
    public ResponseEntity<TaskResponseDTO> addTask(
            @PathVariable Long adId, 
            @Valid @RequestBody TaskRequestDTO requestDTO) {
        TaskResponseDTO response = taskService.addTask(adId, requestDTO);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/advertisement/{adId}")
    public ResponseEntity<List<TaskResponseDTO>> listTasks(@PathVariable Long adId) {
        List<TaskResponseDTO> tasks = taskService.listTasksByGroup(adId);
        return ResponseEntity.ok(tasks);
    }

    @PatchMapping("/{taskId}/toggle")
    public ResponseEntity<TaskResponseDTO> toggleTask(@PathVariable Long taskId) {
        TaskResponseDTO updatedTask = taskService.toggleTaskStatus(taskId);
        return ResponseEntity.ok(updatedTask);
    }

    @DeleteMapping("/{taskId}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long taskId) {
        taskService.deleteTask(taskId);
        return ResponseEntity.noContent().build();
    }

}
