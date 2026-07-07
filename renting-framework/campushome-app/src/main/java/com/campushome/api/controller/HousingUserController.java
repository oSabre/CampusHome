package com.campushome.api.controller;

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

import com.campushome.api.dto.LoginRequestDTO;
import com.campushome.api.dto.LoginResponseDTO;
import com.campushome.api.dto.OwnerRegistrationDTO;
import com.campushome.api.dto.StudentRegistrationDTO;
import com.campushome.api.dto.UserResponseDTO;
import com.campushome.api.service.HousingUserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class HousingUserController {

    private final HousingUserService housingUserService;

    @PostMapping("/register/student")
    public ResponseEntity<UserResponseDTO> registerStudent(@RequestBody StudentRegistrationDTO request) {
        return new ResponseEntity<>(housingUserService.registerStudent(request), HttpStatus.CREATED);
    }

    @PostMapping("/register/owner")
    public ResponseEntity<UserResponseDTO> registerOwner(@RequestBody OwnerRegistrationDTO request) {
        return new ResponseEntity<>(housingUserService.registerOwner(request), HttpStatus.CREATED);
    }

    @GetMapping("/profile/{id}")
    public ResponseEntity<UserResponseDTO> getProfile(@PathVariable Long id) {
        return ResponseEntity.ok(housingUserService.getProfile(id));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody LoginRequestDTO loginRequestDTO) {
        return ResponseEntity.ok(housingUserService.login(loginRequestDTO));
    }

    @PatchMapping("/{id}/bio")
    public ResponseEntity<Void> updateBio(@PathVariable Long id, @RequestBody Map<String, String> body) {
        housingUserService.updateBio(id, body.get("bio"));
        return ResponseEntity.noContent().build();
    }
}