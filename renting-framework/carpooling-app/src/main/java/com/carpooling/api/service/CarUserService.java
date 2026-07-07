package com.carpooling.api.service;

import org.springframework.stereotype.Service;

import com.carpooling.api.dto.DriverRegistrationDTO;
import com.carpooling.api.dto.DriverResponseDTO;
import com.carpooling.api.dto.LoginRequestDTO;
import com.carpooling.api.dto.LoginResponseDTO;
import com.carpooling.api.model.CarUser;
import com.carpooling.api.repository.CarUserRepository;
import com.rentingframework.core.exception.ResourceNotFoundException;
import com.rentingframework.core.model.User;
import com.rentingframework.core.service.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CarUserService {

    private final UserService userService;
    private final CarUserRepository carUserRepository;

    public DriverResponseDTO registerDriver(DriverRegistrationDTO request) {
        CarUser user = new CarUser();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        user.setPhone(request.getTelefone());
        user.setBio(request.getBio());
        user.setDriverLicenseNumber(request.getDriverLicenseNumber());
        user.setYearsLicensed(request.getYearsLicensed());

        CarUser saved = (CarUser) userService.registerUser(user);
        return convertToResponseDTO(saved);
    }

    public LoginResponseDTO login(LoginRequestDTO request) {
        User user = userService.login(request.getEmail(), request.getPassword());
        CarUser carUser = (CarUser) user;

        return LoginResponseDTO.builder()
                .id(carUser.getId())
                .name(carUser.getName())
                .build();
    }

    public DriverResponseDTO getProfile(Long id) {
        User user = userService.findById(id);
        return convertToResponseDTO((CarUser) user);
    }

    public void updateBio(Long id, String bio) {
        CarUser user = carUserRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Motorista não encontrado."));
        user.setBio(bio);
        carUserRepository.save(user);
    }

    private DriverResponseDTO convertToResponseDTO(CarUser user) {
        return new DriverResponseDTO(
            user.getId(),
            user.getName(),
            user.getEmail(),
            user.getBio(),
            user.getPhone(),
            user.getDriverLicenseNumber(),
            user.getYearsLicensed()
        );
    }
}
