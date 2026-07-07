package com.rentingframework.core.dto;
 
import java.time.LocalDateTime;
import java.util.List;
 
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
 
/**
 * Covers only what's generic about a Group (who's in it, when it formed,
 * which listing it's attached to). If an app's Group subclass adds fields
 * (rules, maintenance log, session notes), the app builds its own richer
 * DTO around those — this one is a convenience for the common part, not
 * a replacement for it.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GroupResponseDTO {
    private Long id;
    private Long listingId;
    private List<UserSummaryDTO> members;
    private LocalDateTime createdAt;
}