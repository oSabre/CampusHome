package com.carpooling.api.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JoinRequestDTO {
    private Long driverId;
    private Long listingId;
}