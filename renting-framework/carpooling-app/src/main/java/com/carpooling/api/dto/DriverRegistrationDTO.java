package com.carpooling.api.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DriverRegistrationDTO {
    private String name;
    private String email;
    private String password;
    private String telefone;
    private String bio;
    private String driverLicenseNumber;
    private Integer yearsLicensed;
}