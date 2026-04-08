package com.campushome.api.dto;

import com.campushome.api.enums.UserRole;

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
    private String course;
    private String bio;
    private String telefone;
    private String cpfCnpj;
    private UserRole role;
}
