package com.carpooling.api.dto;

import java.time.LocalDateTime;

import com.carpooling.api.enums.BookingStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingResponseDTO {
    private Long id;
    private Long driverId;
    private String driverName;
    private Long vehicleListingId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private BookingStatus status;
    private Integer odometerStart;
    private Integer odometerEnd;
}