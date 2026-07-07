package com.expert.api.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SpecialistRegistrationDTO {
    private String name;
    private String email;
    private String password;
    private String telefone;
    private String bio;
    private String specialty;
    private String credentials;
}