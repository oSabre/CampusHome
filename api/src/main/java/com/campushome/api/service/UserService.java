package com.campushome.api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.campushome.api.dto.OwnerRegistrationDTO;
import com.campushome.api.dto.StudentRegistrationDTO;
import com.campushome.api.dto.UserResponseDTO;
import com.campushome.api.enums.UserRole;
import com.campushome.api.model.User;
import com.campushome.api.repository.UserRepository;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

import com.campushome.api.dto.LoginRequestDTO;
import com.campushome.api.dto.LoginResponseDTO;

@Service
@RequiredArgsConstructor
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public UserResponseDTO registerStudent(StudentRegistrationDTO request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Este e-mail já está em uso.");
        }

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        user.setCourse(request.getCourse());
        user.setBio(request.getBio());
        user.setTelefone(request.getTelefone());
        user.setRole(UserRole.STUDENT);

        User savedUser = userRepository.save(user);

        return convertToResponseDTO(savedUser);
    }

    public UserResponseDTO registerOwner(OwnerRegistrationDTO request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Este e-mail já está em uso.");
        }

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        user.setCpfCnpj(request.getCpfCnpj());
        user.setBio(request.getBio());
        user.setTelefone(request.getTelefone());
        user.setRole(UserRole.OWNER);

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
            user.getBio(),
            user.getTelefone(),
            user.getCpfCnpj(),
            user.getRole()
        );
    }

    @Transactional(readOnly = true)
    public LoginResponseDTO login(LoginRequestDTO request){
        User user = userRepository.findByEmail(request.getEmail()).orElseThrow(()->new RuntimeException("E-mail não cadastrado"));

        if(!user.getPassword().equals(request.getPassword())){
            throw new RuntimeException("Senha Incorreta");
        }

        return LoginResponseDTO.builder()
                .id(user.getId())
                .name(user.getName())
                .role(user.getRole())
                .build();
    }

    @Transactional
    public void updateBio(Long id, String bio) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        user.setBio(bio);
        userRepository.save(user);
    }

}