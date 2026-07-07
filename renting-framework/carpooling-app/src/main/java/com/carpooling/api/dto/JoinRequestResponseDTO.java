package com.carpooling.api.dto;

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
public class JoinRequestResponseDTO {
    private Long id;
    private Long driverId;
    private String driverName;
    private Long listingId;
    private String vehicleTitle;
    private RequestStatus status;
    private LocalDateTime createdAt;
    private int matchScore;
    private String matchJustification;
    private String driverBio;

    // Same as InterestResponseDTO's constructor - name, bio, title, id are
    // all on core's base User/Listing, no CarUser/VehicleListing cast needed.
    public JoinRequestResponseDTO(Request request, int matchScore, String matchJustification) {
        this.id = request.getId();
        this.driverId = request.getRequester().getId();
        this.driverName = request.getRequester().getName();
        this.listingId = request.getListing().getId();
        this.vehicleTitle = request.getListing().getTitle();
        this.status = request.getStatus();
        this.createdAt = request.getCreatedAt();
        this.matchScore = matchScore;
        this.matchJustification = matchJustification;
        this.driverBio = request.getRequester().getBio();
    }
}