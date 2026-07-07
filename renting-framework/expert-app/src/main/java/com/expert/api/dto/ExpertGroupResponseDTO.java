package com.expert.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Unlike Housing/Carpooling's generic member-list shape (needed there
 * since N is variable), this app's Group is always exactly 2 members -
 * so the DTO can be explicit about who's who instead of making the
 * consumer guess which array element is the client and which is the
 * specialist.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExpertGroupResponseDTO {
    private Long id;
    private Long listingId;
    private String listingTitle;
    private String engagementNotes;
    private ParticipantSummaryDTO client;
    private ParticipantSummaryDTO specialist;
}