package com.expert.api.dto;

import com.expert.api.enums.UserRole;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserResponseDTO {
    private Long id;
    private String name;
    private String email;
    private String bio;
    private String telefone;
    private String specialty;
    private String credentials;
    private UserRole role;
}