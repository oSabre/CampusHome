package com.campushome.api.dto;

import java.time.LocalDateTime;

import com.rentingframework.core.enums.RequestStatus;
import com.rentingframework.core.model.Request;

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
    private RequestStatus status;
    private LocalDateTime createdAt;
    private int matchScore;
    private String matchJustification;
    private String studentBio;

    // None of these getters need a CampusUser/HousingListing cast — name,
    // bio, title, id are all on core's base User/Listing.
    public InterestResponseDTO(Request request, int matchScore, String matchJustification) {
        this.id = request.getId();
        this.studentId = request.getRequester().getId();
        this.studentName = request.getRequester().getName();
        this.adId = request.getListing().getId();
        this.adTitle = request.getListing().getTitle();
        this.status = request.getStatus();
        this.createdAt = request.getCreatedAt();
        this.matchScore = matchScore;
        this.matchJustification = matchJustification;
        this.studentBio = request.getRequester().getBio();
    }
}