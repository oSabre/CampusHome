package com.expert.api.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.expert.api.dto.TaskRequestDTO;
import com.expert.api.dto.TaskResponseDTO;
import com.expert.api.enums.TaskType;
import com.expert.api.model.ExpertTask;
import com.rentingframework.core.exception.ResourceNotFoundException;
import com.rentingframework.core.model.Group;
import com.rentingframework.core.model.Task;
import com.rentingframework.core.repository.GroupRepository;
import com.rentingframework.core.service.TaskService;

import lombok.RequiredArgsConstructor;

/**
 * Same shape as CarpoolingTaskService: translates listingId to groupId
 * (a Task hangs off a Group, not a Listing directly), and builds its own
 * DTO since ExpertTask's taskType isn't something core's toResponseDto()
 * knows about.
 */
@Service
@RequiredArgsConstructor
public class ExpertTaskService {

    private final TaskService taskService;
    private final GroupRepository groupRepository;

    public TaskResponseDTO addTask(Long listingId, TaskRequestDTO requestDTO) {
        return addTask(listingId, requestDTO.getDescription(), requestDTO.getTaskType(), null);
    }

    /**
     * Used both by the manual "add task" endpoint and internally by
     * SessionService when auto-generating SESSION_NOTES/INVOICE tasks on
     * session completion - assignedUserId is null for manual tasks
     * (claimed later via toggle) but set immediately for auto-generated
     * ones (assigned straight to the specialist).
     */
    public TaskResponseDTO addTask(Long listingId, String description, TaskType taskType, Long assignedUserId) {
        Group group = groupOrThrow(listingId);

        ExpertTask task = new ExpertTask();
        task.setDescription(description);
        task.setTaskType(taskType);

        Task saved = taskService.addTask(task, group.getId(), assignedUserId);
        return toResponseDto((ExpertTask) saved);
    }

    public List<TaskResponseDTO> listTasks(Long listingId) {
        Group group = groupOrThrow(listingId);

        return taskService.listByGroup(group.getId()).stream()
                .map(t -> toResponseDto((ExpertTask) t))
                .collect(Collectors.toList());
    }

    public TaskResponseDTO toggleTask(Long taskId, Long userId) {
        Task saved = taskService.toggleStatus(taskId, userId);
        return toResponseDto((ExpertTask) saved);
    }

    public void deleteTask(Long taskId) {
        taskService.deleteTask(taskId);
    }

    private TaskResponseDTO toResponseDto(ExpertTask task) {
        return new TaskResponseDTO(task.getId(), task.getDescription(), task.isCompleted(), task.getTaskType());
    }

    private Group groupOrThrow(Long listingId) {
        return groupRepository.findByListingId(listingId)
                .orElseThrow(() -> new ResourceNotFoundException("Grupo não encontrado para este anúncio."));
    }
}