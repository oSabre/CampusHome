package com.campushome.api.service;

import org.springframework.stereotype.Service;

import com.campushome.api.dto.LoginRequestDTO;
import com.campushome.api.dto.LoginResponseDTO;
import com.campushome.api.dto.OwnerRegistrationDTO;
import com.campushome.api.dto.StudentRegistrationDTO;
import com.campushome.api.dto.UserResponseDTO;
import com.campushome.api.enums.UserRole;
import com.campushome.api.model.CampusUser;
import com.campushome.api.repository.CampusUserRepository;
import com.rentingframework.core.exception.ResourceNotFoundException;
import com.rentingframework.core.model.User;
import com.rentingframework.core.service.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class HousingUserService {

    private final UserService userService;
    private final CampusUserRepository campusUserRepository;

    public UserResponseDTO registerStudent(StudentRegistrationDTO request) {
        CampusUser user = new CampusUser();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        user.setPhone(request.getTelefone());
        user.setBio(request.getBio());
        user.setCourse(request.getCourse());
        user.setRole(UserRole.STUDENT);

        CampusUser saved = (CampusUser) userService.registerUser(user);
        return convertToResponseDTO(saved);
    }

    public UserResponseDTO registerOwner(OwnerRegistrationDTO request) {
        CampusUser user = new CampusUser();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        user.setPhone(request.getTelefone());
        user.setBio(request.getBio());
        user.setCpfCnpj(request.getCpfCnpj());
        user.setRole(UserRole.OWNER);

        CampusUser saved = (CampusUser) userService.registerUser(user);
        return convertToResponseDTO(saved);
    }

    public LoginResponseDTO login(LoginRequestDTO request) {
        User user = userService.login(request.getEmail(), request.getPassword());
        CampusUser campusUser = (CampusUser) user;

        return LoginResponseDTO.builder()
                .id(campusUser.getId())
                .name(campusUser.getName())
                .role(campusUser.getRole())
                .build();
    }

    public UserResponseDTO getProfile(Long id) {
        User user = userService.findById(id);
        return convertToResponseDTO((CampusUser) user);
    }

    // Goes through CampusUserRepository directly, not userService.registerUser(),
    // since this is a plain field update on an existing user, not a new
    // registration — running it through registerUser() would incorrectly
    // reject the user's own (already-registered) email as taken.
    public void updateBio(Long id, String bio) {
        CampusUser user = campusUserRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado."));
        user.setBio(bio);
        campusUserRepository.save(user);
    }

    private UserResponseDTO convertToResponseDTO(CampusUser user) {
        return new UserResponseDTO(
            user.getId(),
            user.getName(),
            user.getEmail(),
            user.getCourse(),
            user.getBio(),
            user.getPhone(),
            user.getCpfCnpj(),
            user.getRole()
        );
    }
}