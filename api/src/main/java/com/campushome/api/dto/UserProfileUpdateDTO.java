package com.campushome.api.dto;

import com.campushome.api.model.UserRole;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserProfileUpdateDTO{
    private String name;
    private String course;
    private String bio;
    private String preferences;
    private UserRole role;
}