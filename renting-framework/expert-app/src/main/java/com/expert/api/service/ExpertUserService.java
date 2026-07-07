package com.expert.api.service;

import org.springframework.stereotype.Service;

import com.expert.api.dto.ClientRegistrationDTO;
import com.expert.api.dto.LoginRequestDTO;
import com.expert.api.dto.LoginResponseDTO;
import com.expert.api.dto.SpecialistRegistrationDTO;
import com.expert.api.dto.UserResponseDTO;
import com.expert.api.enums.UserRole;
import com.expert.api.model.ExpertUser;
import com.expert.api.repository.ExpertUserRepository;
import com.rentingframework.core.exception.ResourceNotFoundException;
import com.rentingframework.core.model.User;
import com.rentingframework.core.service.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ExpertUserService {

    private final UserService userService;
    private final ExpertUserRepository expertUserRepository;

    public UserResponseDTO registerClient(ClientRegistrationDTO request) {
        ExpertUser user = new ExpertUser();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        user.setPhone(request.getTelefone());
        user.setBio(request.getBio());
        user.setRole(UserRole.CLIENT);

        ExpertUser saved = (ExpertUser) userService.registerUser(user);
        return convertToResponseDTO(saved);
    }

    public UserResponseDTO registerSpecialist(SpecialistRegistrationDTO request) {
        ExpertUser user = new ExpertUser();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        user.setPhone(request.getTelefone());
        user.setBio(request.getBio());
        user.setSpecialty(request.getSpecialty());
        user.setCredentials(request.getCredentials());
        user.setRole(UserRole.SPECIALIST);

        ExpertUser saved = (ExpertUser) userService.registerUser(user);
        return convertToResponseDTO(saved);
    }

    public LoginResponseDTO login(LoginRequestDTO request) {
        User user = userService.login(request.getEmail(), request.getPassword());
        ExpertUser expertUser = (ExpertUser) user;

        return LoginResponseDTO.builder()
                .id(expertUser.getId())
                .name(expertUser.getName())
                .role(expertUser.getRole())
                .build();
    }

    public UserResponseDTO getProfile(Long id) {
        User user = userService.findById(id);
        return convertToResponseDTO((ExpertUser) user);
    }

    public void updateBio(Long id, String bio) {
        ExpertUser user = expertUserRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado."));
        user.setBio(bio);
        expertUserRepository.save(user);
    }

    private UserResponseDTO convertToResponseDTO(ExpertUser user) {
        return new UserResponseDTO(
            user.getId(),
            user.getName(),
            user.getEmail(),
            user.getBio(),
            user.getPhone(),
            user.getSpecialty(),
            user.getCredentials(),
            user.getRole()
        );
    }
}