package com.campushome.api.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.campushome.api.dto.TaskRequestDTO;
import com.campushome.api.service.HousingTaskService;
import com.rentingframework.core.dto.TaskResponseDTO;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/tasks")
@RequiredArgsConstructor
public class HousingTaskController {

    private final HousingTaskService housingTaskService;

    @PostMapping("/advertisement/{adId}")
    public ResponseEntity<TaskResponseDTO> addTask(
            @PathVariable Long adId,
            @Valid @RequestBody TaskRequestDTO requestDTO) {
        return new ResponseEntity<>(housingTaskService.addTask(adId, requestDTO), HttpStatus.CREATED);
    }

    @GetMapping("/advertisement/{adId}")
    public ResponseEntity<List<TaskResponseDTO>> listTasks(@PathVariable Long adId) {
        return ResponseEntity.ok(housingTaskService.listTasks(adId));
    }

    @PatchMapping("/{taskId}/toggle")
    public ResponseEntity<TaskResponseDTO> toggleTask(@PathVariable Long taskId, @RequestParam Long userId) {
        return ResponseEntity.ok(housingTaskService.toggleTask(taskId, userId));
    }

    @DeleteMapping("/{taskId}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long taskId) {
        housingTaskService.deleteTask(taskId);
        return ResponseEntity.noContent().build();
    }
}