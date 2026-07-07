package com.carpooling.api.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.carpooling.api.dto.DriverRegistrationDTO;
import com.carpooling.api.dto.DriverResponseDTO;
import com.carpooling.api.dto.LoginRequestDTO;
import com.carpooling.api.dto.LoginResponseDTO;
import com.carpooling.api.service.CarUserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class CarUserController {

    private final CarUserService carUserService;

    @PostMapping("/register")
    public ResponseEntity<DriverResponseDTO> register(@RequestBody DriverRegistrationDTO request) {
        return new ResponseEntity<>(carUserService.registerDriver(request), HttpStatus.CREATED);
    }

    @GetMapping("/profile/{id}")
    public ResponseEntity<DriverResponseDTO> getProfile(@PathVariable Long id) {
        return ResponseEntity.ok(carUserService.getProfile(id));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody LoginRequestDTO loginRequestDTO) {
        return ResponseEntity.ok(carUserService.login(loginRequestDTO));
    }

    @PatchMapping("/{id}/bio")
    public ResponseEntity<Void> updateBio(@PathVariable Long id, @RequestBody Map<String, String> body) {
        carUserService.updateBio(id, body.get("bio"));
        return ResponseEntity.noContent().build();
    }
}