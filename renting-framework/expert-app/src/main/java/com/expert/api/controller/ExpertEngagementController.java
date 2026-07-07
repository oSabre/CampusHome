package com.expert.api.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.expert.api.dto.EngagementRequestDTO;
import com.expert.api.dto.EngagementResponseDTO;
import com.expert.api.service.ExpertEngagementService;
import com.rentingframework.core.enums.RequestStatus;

import lombok.RequiredArgsConstructor;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/engagements")
@RequiredArgsConstructor
public class ExpertEngagementController {

    private final ExpertEngagementService expertEngagementService;

    @PostMapping
    public ResponseEntity<EngagementResponseDTO> createRequest(@RequestBody EngagementRequestDTO request) {
        return new ResponseEntity<>(expertEngagementService.createEngagementRequest(request), HttpStatus.CREATED);
    }

    @GetMapping("/listing/{listingId}")
    public ResponseEntity<List<EngagementResponseDTO>> getRequestsByListing(@PathVariable Long listingId) {
        return ResponseEntity.ok(expertEngagementService.getRequestsByListing(listingId));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<EngagementResponseDTO> updateStatus(
            @PathVariable Long id,
            @RequestParam RequestStatus status) {
        return ResponseEntity.ok(expertEngagementService.updateStatus(id, status));
    }

    @GetMapping("/owner/{ownerId}/pending")
    public ResponseEntity<List<EngagementResponseDTO>> getPendingRequests(@PathVariable Long ownerId) {
        return ResponseEntity.ok(expertEngagementService.getPendingRequestsForOwner(ownerId));
    }
}