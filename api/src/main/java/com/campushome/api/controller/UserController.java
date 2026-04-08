package com.campushome.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.campushome.api.dto.OwnerRegistrationDTO;
import com.campushome.api.dto.StudentRegistrationDTO;
import com.campushome.api.dto.UserResponseDTO;
import com.campushome.api.service.UserService;


@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/register/student")
    public ResponseEntity<UserResponseDTO> registerStudent(@RequestBody StudentRegistrationDTO request) {
        UserResponseDTO response = userService.registerStudent(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/register/owner")
    public ResponseEntity<UserResponseDTO> registerOwner(@RequestBody OwnerRegistrationDTO request) {
        UserResponseDTO response = userService.registerOwner(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/profile/{id}")
    public ResponseEntity<UserResponseDTO> getProfile(@PathVariable Long id) {
        UserResponseDTO response = userService.getProfile(id);
        return ResponseEntity.ok(response);
    }
    
    
}
