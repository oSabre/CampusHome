package com.carpooling.api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class DriverResponseDTO {
    private Long id;
    private String name;
    private String email;
    private String bio;
    private String telefone;
    private String driverLicenseNumber;
    private Integer yearsLicensed;
}