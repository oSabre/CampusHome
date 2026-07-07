package com.expert.api.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ExpertListingResponseDTO {
    private Long id;
    private String title;
    private String specialty;
    private BigDecimal hourlyRate;
    private String specialistName;
    private boolean active;
}