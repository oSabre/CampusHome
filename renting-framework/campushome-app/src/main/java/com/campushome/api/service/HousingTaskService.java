package com.campushome.api.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.campushome.api.dto.TaskRequestDTO;
import com.rentingframework.core.dto.TaskResponseDTO;
import com.rentingframework.core.exception.ResourceNotFoundException;
import com.rentingframework.core.model.Group;
import com.rentingframework.core.model.Task;
import com.rentingframework.core.repository.GroupRepository;
import com.rentingframework.core.service.TaskService;

import lombok.RequiredArgsConstructor;

/**
 * The original API takes an adId (listing id) on every task endpoint, but
 * core's TaskService works in terms of groupId — a Task hangs off a Group,
 * not a Listing directly. This translates one to the other so the
 * controller's URL shape doesn't have to change.
 */
@Service
@RequiredArgsConstructor
public class HousingTaskService {

    private final TaskService taskService;
    private final GroupRepository groupRepository;

    public TaskResponseDTO addTask(Long adId, TaskRequestDTO requestDTO) {
        Group group = groupOrThrow(adId);

        Task task = new Task();
        task.setDescription(requestDTO.getDescription());

        Task saved = taskService.addTask(task, group.getId(), null);
        return taskService.toResponseDto(saved);
    }

    public List<TaskResponseDTO> listTasks(Long adId) {
        Group group = groupOrThrow(adId);

        return taskService.listByGroup(group.getId()).stream()
                .map(taskService::toResponseDto)
                .collect(Collectors.toList());
    }

    public TaskResponseDTO toggleTask(Long taskId, Long userId) {
        Task saved = taskService.toggleStatus(taskId, userId);
        return taskService.toResponseDto(saved);
    }

    public void deleteTask(Long taskId) {
        taskService.deleteTask(taskId);
    }

    private Group groupOrThrow(Long adId) {
        return groupRepository.findByListingId(adId)
                .orElseThrow(() -> new ResourceNotFoundException("Grupo não encontrado para este anúncio."));
    }
}