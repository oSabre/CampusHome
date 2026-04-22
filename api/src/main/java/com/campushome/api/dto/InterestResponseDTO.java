package com.campushome.api.dto;

import java.time.LocalDateTime;

import com.campushome.api.enums.InterestStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class InterestResponseDTO {
    private Long id;
    private Long studentId;
    private String studentName;
    private Long adId;
    private String adTitle;
    private InterestStatus status;
    private LocalDateTime createdAt;
}
