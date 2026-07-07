package com.expert.api.dto;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExpertListingRequestDTO {
    private String title;
    private String description;
    private String specialty;
    private BigDecimal hourlyRate;
    private Long userId;
}