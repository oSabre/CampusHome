package com.expert.api.dto;

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
public class EngagementResponseDTO {
    private Long id;
    private Long clientId;
    private String clientName;
    private Long listingId;
    private String listingTitle;
    private RequestStatus status;
    private LocalDateTime createdAt;
    private int matchScore;
    private String matchJustification;
    private String clientBio;

    public EngagementResponseDTO(Request request, int matchScore, String matchJustification) {
        this.id = request.getId();
        this.clientId = request.getRequester().getId();
        this.clientName = request.getRequester().getName();
        this.listingId = request.getListing().getId();
        this.listingTitle = request.getListing().getTitle();
        this.status = request.getStatus();
        this.createdAt = request.getCreatedAt();
        this.matchScore = matchScore;
        this.matchJustification = matchJustification;
        this.clientBio = request.getRequester().getBio();
    }
}