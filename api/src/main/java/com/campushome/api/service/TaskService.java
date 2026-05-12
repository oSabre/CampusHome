package com.campushome.api.service;

import com.campushome.api.dto.TaskRequestDTO;
import com.campushome.api.dto.TaskResponseDTO;
import com.campushome.api.model.HousingGroup;
import com.campushome.api.model.Task;
import com.campushome.api.model.User;
import com.campushome.api.repository.HousingGroupRepository;
import com.campushome.api.repository.TaskRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import com.campushome.api.repository.UserRepository;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;

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
    public TaskResponseDTO toggleTaskStatus(Long taskId, Long userId) {
        Task task = taskRepository.findById(taskId)
            .orElseThrow(() -> new RuntimeException("Tarefa não encontrada"));
        
        boolean newStatus = !task.isCompleted();
        task.setCompleted(newStatus);

        User user = userRepository.findById(userId)
        .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        int xpValue = 10;
        int currentXp = (user.getXp() == null) ? 0 : user.getXp();

        if (newStatus) {
            user.setXp(currentXp + xpValue);
            // Opcional: Atribui a tarefa a quem terminou para registro
            task.setAssignedUser(user); 
        } else {
            user.setXp(Math.max(0, currentXp - xpValue));
        }

        userRepository.save(user);
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
