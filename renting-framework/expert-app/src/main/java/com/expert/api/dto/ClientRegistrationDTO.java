package com.expert.api.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClientRegistrationDTO {
    private String name;
    private String email;
    private String password;
    private String telefone;
    private String bio;
}