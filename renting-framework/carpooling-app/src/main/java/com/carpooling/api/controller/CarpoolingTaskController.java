package com.carpooling.api.controller;

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

import com.carpooling.api.dto.TaskRequestDTO;
import com.carpooling.api.dto.TaskResponseDTO;
import com.carpooling.api.service.CarpoolingTaskService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/tasks")
@RequiredArgsConstructor
public class CarpoolingTaskController {

    private final CarpoolingTaskService carpoolingTaskService;

    @PostMapping("/vehicle/{listingId}")
    public ResponseEntity<TaskResponseDTO> addTask(
            @PathVariable Long listingId,
            @Valid @RequestBody TaskRequestDTO requestDTO) {
        return new ResponseEntity<>(carpoolingTaskService.addTask(listingId, requestDTO), HttpStatus.CREATED);
    }

    @GetMapping("/vehicle/{listingId}")
    public ResponseEntity<List<TaskResponseDTO>> listTasks(@PathVariable Long listingId) {
        return ResponseEntity.ok(carpoolingTaskService.listTasks(listingId));
    }

    @PatchMapping("/{taskId}/toggle")
    public ResponseEntity<TaskResponseDTO> toggleTask(@PathVariable Long taskId, @RequestParam Long userId) {
        return ResponseEntity.ok(carpoolingTaskService.toggleTask(taskId, userId));
    }

    @DeleteMapping("/{taskId}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long taskId) {
        carpoolingTaskService.deleteTask(taskId);
        return ResponseEntity.noContent().build();
    }
}