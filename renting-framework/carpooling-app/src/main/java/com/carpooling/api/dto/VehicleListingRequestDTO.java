package com.carpooling.api.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VehicleListingRequestDTO {
    private String title;
    private String description;
    private String make;
    private String model;
    private Integer year;
    private String licensePlate;
    private Integer mileage;
    private Long userId;
}