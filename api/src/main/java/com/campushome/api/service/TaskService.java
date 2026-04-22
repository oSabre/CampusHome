package com.campushome.api.service;

import com.campushome.api.dto.TaskRequestDTO;
import com.campushome.api.dto.TaskResponseDTO;
import com.campushome.api.model.HousingGroup;
import com.campushome.api.model.Task;
import com.campushome.api.repository.HousingGroupRepository;
import com.campushome.api.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired 
    private HousingGroupRepository housingGroupRepository;

    @Transactional
    public TaskResponseDTO addTask(Long adId, TaskRequestDTO requestDTO) {
        HousingGroup group = housingGroupRepository.findByAdvertisementId(adId)
            .orElseThrow(() -> new RuntimeException("Grupo não encontrado para este anúncio"));

        Task task = new Task();
        task.setDescription(requestDTO.getDescription());
        task.setCompleted(false);
        task.setHousingGroup(group);

        Task savedTask = taskRepository.save(task);
        return convertToResponseDTO(savedTask);
    }

    @Transactional(readOnly = true)
    public List<TaskResponseDTO> listTasksByGroup(Long adId) {
        return taskRepository.findByHousingGroupAdvertisementId(adId)
                .stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public TaskResponseDTO toggleTaskStatus(Long taskId) {
        Task task = taskRepository.findById(taskId)
            .orElseThrow(() -> new RuntimeException("Tarefa não encontrada"));
        
        task.setCompleted(!task.isCompleted());
        return convertToResponseDTO(taskRepository.save(task));
    }

    @Transactional
    public void deleteTask(Long taskId) {
        if (!taskRepository.existsById(taskId)) {
            throw new RuntimeException("Tarefa não encontrada");
        }
        taskRepository.deleteById(taskId);
    }

    private TaskResponseDTO convertToResponseDTO(Task task) {
        TaskResponseDTO response = new TaskResponseDTO();
        response.setId(task.getId());
        response.setDescription(task.getDescription());
        response.setCompleted(task.isCompleted());
        return response;
    }
}
