package com.carpooling.api.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.carpooling.api.dto.TaskRequestDTO;
import com.carpooling.api.dto.TaskResponseDTO;
import com.carpooling.api.enums.TaskType;
import com.carpooling.api.model.CarpoolingTask;
import com.rentingframework.core.exception.ResourceNotFoundException;
import com.rentingframework.core.model.Group;
import com.rentingframework.core.model.Task;
import com.rentingframework.core.repository.GroupRepository;
import com.rentingframework.core.service.TaskService;

import lombok.RequiredArgsConstructor;

/**
 * Same adId (listing) -> groupId translation as HousingTaskService.
 * Unlike Housing's TaskResponseDTO (reused directly from core since
 * HousingTask adds nothing), this builds its own DTO here rather than
 * calling core's toResponseDto(), since that one doesn't know about
 * taskType.
 */
@Service
@RequiredArgsConstructor
public class CarpoolingTaskService {

    private final TaskService taskService;
    private final GroupRepository groupRepository;

    public TaskResponseDTO addTask(Long listingId, TaskRequestDTO requestDTO) {
        return addTask(listingId, requestDTO.getDescription(), requestDTO.getTaskType(), null);
    }

    /**
     * Used both by the manual "add task" endpoint and internally by
     * BookingService when auto-generating REFUEL/MILEAGE_LOG tasks on
     * booking completion - assignedUserId is null for manual tasks
     * (claimed later via toggle, same as Housing) but set immediately
     * for auto-generated ones (assigned straight to whoever just drove).
     */
    public TaskResponseDTO addTask(Long listingId, String description, TaskType taskType, Long assignedUserId) {
        Group group = groupOrThrow(listingId);

        CarpoolingTask task = new CarpoolingTask();
        task.setDescription(description);
        task.setTaskType(taskType);

        Task saved = taskService.addTask(task, group.getId(), assignedUserId);
        return toResponseDto((CarpoolingTask) saved);
    }

    public List<TaskResponseDTO> listTasks(Long listingId) {
        Group group = groupOrThrow(listingId);

        return taskService.listByGroup(group.getId()).stream()
                .map(t -> toResponseDto((CarpoolingTask) t))
                .collect(Collectors.toList());
    }

    public TaskResponseDTO toggleTask(Long taskId, Long userId) {
        Task saved = taskService.toggleStatus(taskId, userId);
        return toResponseDto((CarpoolingTask) saved);
    }

    public void deleteTask(Long taskId) {
        taskService.deleteTask(taskId);
    }

    private TaskResponseDTO toResponseDto(CarpoolingTask task) {
        return new TaskResponseDTO(task.getId(), task.getDescription(), task.isCompleted(), task.getTaskType());
    }

    private Group groupOrThrow(Long listingId) {
        return groupRepository.findByListingId(listingId)
                .orElseThrow(() -> new ResourceNotFoundException("Grupo não encontrado para este veículo."));
    }
}