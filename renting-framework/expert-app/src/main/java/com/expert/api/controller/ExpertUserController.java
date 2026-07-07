package com.expert.api.controller;

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

import com.expert.api.dto.ClientRegistrationDTO;
import com.expert.api.dto.LoginRequestDTO;
import com.expert.api.dto.LoginResponseDTO;
import com.expert.api.dto.SpecialistRegistrationDTO;
import com.expert.api.dto.UserResponseDTO;
import com.expert.api.service.ExpertUserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class ExpertUserController {

    private final ExpertUserService expertUserService;

    @PostMapping("/register/client")
    public ResponseEntity<UserResponseDTO> registerClient(@RequestBody ClientRegistrationDTO request) {
        return new ResponseEntity<>(expertUserService.registerClient(request), HttpStatus.CREATED);
    }

    @PostMapping("/register/specialist")
    public ResponseEntity<UserResponseDTO> registerSpecialist(@RequestBody SpecialistRegistrationDTO request) {
        return new ResponseEntity<>(expertUserService.registerSpecialist(request), HttpStatus.CREATED);
    }

    @GetMapping("/profile/{id}")
    public ResponseEntity<UserResponseDTO> getProfile(@PathVariable Long id) {
        return ResponseEntity.ok(expertUserService.getProfile(id));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody LoginRequestDTO loginRequestDTO) {
        return ResponseEntity.ok(expertUserService.login(loginRequestDTO));
    }

    @PatchMapping("/{id}/bio")
    public ResponseEntity<Void> updateBio(@PathVariable Long id, @RequestBody Map<String, String> body) {
        expertUserService.updateBio(id, body.get("bio"));
        return ResponseEntity.noContent().build();
    }
}