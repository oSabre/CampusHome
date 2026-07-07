package com.expert.api.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.expert.api.enums.SessionStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SessionResponseDTO {
    private Long id;
    private Long clientId;
    private String clientName;
    private Long expertListingId;
    private String specialistName;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private SessionStatus status;
    private String notes;
    private BigDecimal invoiceAmount;
}