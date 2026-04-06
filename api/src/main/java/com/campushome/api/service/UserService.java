package com.campushome.api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

        User savedUser = userRepository.save(user);

        return convertToResponseDTO(savedUser);
    }

    public UserResponseDTO getProfile(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado."));
        
        return convertToResponseDTO(user);
    }

    private UserResponseDTO convertToResponseDTO(User user) {
        return new UserResponseDTO(
            user.getId(),
            user.getName(),
            user.getEmail(),
            user.getCourse(),
            user.getBio()
        );
    }
}