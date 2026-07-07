package com.carpooling.api.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CarpoolGroupResponseDTO {
    private Long id;
    private Long listingId;
    private String vehicleTitle;
    private String usagePolicy;
    private List<DriverSummaryDTO> members;
}