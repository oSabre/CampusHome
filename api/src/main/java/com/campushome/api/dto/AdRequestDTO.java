package com.campushome.api.dto;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdRequestDTO {
    private String title;
    private String description;
    private BigDecimal price;
    private String neighborhood;
    private String address;
    private Long userId;
}
