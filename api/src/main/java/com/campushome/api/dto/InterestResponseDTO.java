package com.campushome.api.dto;

import java.time.LocalDateTime;

import com.campushome.api.enums.InterestStatus;
import com.campushome.api.model.Interest;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class InterestResponseDTO {
    private Long id;
    private Long studentId;
    private String studentName;
    private Long adId;
    private String adTitle;
    private InterestStatus status;
    private LocalDateTime createdAt;
    private int matchScore;
    private String matchJustification;
    private String studentBio;

    public InterestResponseDTO(Interest interest, int matchScore, String matchJustification){
        this.id = interest.getId();
        this.studentId = interest.getStudent().getId();
        this.studentName = interest.getStudent().getName();
        this.adId = interest.getAdvertisement().getId();
        this.adTitle = interest.getAdvertisement().getTitle();
        this.status = interest.getStatus();
        this.createdAt = interest.getCreatedAt();
        this.matchScore = matchScore;
        this.matchJustification = matchJustification;
        this.studentBio = interest.getStudent().getBio();
    }

}
