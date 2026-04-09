package com.campushome.api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.campushome.api.dto.UserProfileUpdateDTO;
import com.campushome.api.dto.UserRequestDTO;
import com.campushome.api.dto.UserResponseDTO;
import com.campushome.api.model.User;
import com.campushome.api.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public UserResponseDTO register(UserRequestDTO request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Este e-mail já está em uso.");
        }

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        user.setCourse(request.getCourse());
        user.setBio(request.getBio());
        user.setPreferences(request.getPreferences());

        User savedUser = userRepository.save(user);

        return convertToResponseDTO(savedUser);
    }

    public UserResponseDTO getProfile(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado."));
        
        return convertToResponseDTO(user);
    }
public UserResponseDTO updateProfile(Long id, UserProfileUpdateDTO request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado."));

        if (request.getName() != null) {
            user.setName(request.getName());
        }
        if (request.getCourse() != null) {
            user.setCourse(request.getCourse());
        }
        if (request.getBio() != null) {
            user.setBio(request.getBio());
        }
        if (request.getPreferences() != null) {
            user.setPreferences(request.getPreferences());
        }
        if (request.getRole() != null) {
            user.setRole(request.getRole());
        }

        User updatedUser = userRepository.save(user);
        return convertToResponseDTO(updatedUser);
    }

    private UserResponseDTO convertToResponseDTO(User user) {
        return new UserResponseDTO(
            user.getId(),
            user.getName(),
            user.getEmail(),
            user.getCourse(),
            user.getBio(),
            user.getPreferences()
        );
    }
}