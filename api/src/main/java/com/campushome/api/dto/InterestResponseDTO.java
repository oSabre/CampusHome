package com.campushome.api.dto;

import java.time.LocalDateTime;

import com.campushome.api.enums.InterestStatus;
import com.campushome.api.model.Interest;

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

    public InterestResponseDTO(Interest interest){
        this(
            interest.getId(),
            interest.getStudent().getId(),
            interest.getStudent().getName(),
            interest.getAdvertisement().getId(),
            interest.getAdvertisement().getTitle(),
            interest.getStatus(),
            interest.getCreatedAt()
        );
    }

}
