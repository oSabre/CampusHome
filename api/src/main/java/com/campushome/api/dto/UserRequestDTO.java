package com.campushome.api.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRequestDTO {
    private String name;
    private String email;
    private String password;
    private String course;
    private String bio;
    private String preferences;
}
