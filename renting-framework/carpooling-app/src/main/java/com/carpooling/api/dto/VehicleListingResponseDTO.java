package com.carpooling.api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class VehicleListingResponseDTO {
    private Long id;
    private String title;
    private String make;
    private String model;
    private Integer year;
    private String licensePlate;
    private String ownerName;
    private boolean active;
}