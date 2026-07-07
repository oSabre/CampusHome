package com.campushome.api.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HousingGroupResponseDTO {
    private Long id;
    private Long listingId;
    private String listingTitle;
    private String houseRules;
    private List<ResidentDTO> residents;
}