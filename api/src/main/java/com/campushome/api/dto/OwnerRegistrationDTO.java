package com.campushome.api.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OwnerRegistrationDTO {
    private String name;
    private String email;
    private String password;
    private String telefone;
    private String cpfCnpj;
    private String bio;
}
