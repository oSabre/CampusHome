package com.expert.api.controller;

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

import com.expert.api.dto.TaskRequestDTO;
import com.expert.api.dto.TaskResponseDTO;
import com.expert.api.service.ExpertTaskService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/tasks")
@RequiredArgsConstructor
public class ExpertTaskController {

    private final ExpertTaskService expertTaskService;

    @PostMapping("/listing/{listingId}")
    public ResponseEntity<TaskResponseDTO> addTask(
            @PathVariable Long listingId,
            @Valid @RequestBody TaskRequestDTO requestDTO) {
        return new ResponseEntity<>(expertTaskService.addTask(listingId, requestDTO), HttpStatus.CREATED);
    }

    @GetMapping("/listing/{listingId}")
    public ResponseEntity<List<TaskResponseDTO>> listTasks(@PathVariable Long listingId) {
        return ResponseEntity.ok(expertTaskService.listTasks(listingId));
    }

    @PatchMapping("/{taskId}/toggle")
    public ResponseEntity<TaskResponseDTO> toggleTask(@PathVariable Long taskId, @RequestParam Long userId) {
        return ResponseEntity.ok(expertTaskService.toggleTask(taskId, userId));
    }

    @DeleteMapping("/{taskId}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long taskId) {
        expertTaskService.deleteTask(taskId);
        return ResponseEntity.noContent().build();
    }
}