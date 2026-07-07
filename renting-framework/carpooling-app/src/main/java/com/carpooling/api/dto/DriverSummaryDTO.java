package com.carpooling.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DriverSummaryDTO {
    private Long id;
    private String name;
    private String driverLicenseNumber;
    private Integer reputationScore;
}