package com.expert.api.dto;

import com.expert.api.enums.UserRole;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ParticipantSummaryDTO {
    private Long id;
    private String name;
    private UserRole role;
    private String specialty;
    private Integer reputationScore;
}