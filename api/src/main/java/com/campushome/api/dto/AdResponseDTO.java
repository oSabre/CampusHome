package com.campushome.api.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class AdResponseDTO {
    private Long id;
    private String title;
    private BigDecimal price;
    private String neighborhood;
    private String ownerName;
    private Boolean active;
}
