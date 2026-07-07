package com.carpooling.api.controller;

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

import com.carpooling.api.dto.JoinRequestDTO;
import com.carpooling.api.dto.JoinRequestResponseDTO;
import com.carpooling.api.service.PoolRequestService;
import com.rentingframework.core.enums.RequestStatus;

import lombok.RequiredArgsConstructor;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/pool-requests")
@RequiredArgsConstructor
public class PoolRequestController {

    private final PoolRequestService poolRequestService;

    @PostMapping
    public ResponseEntity<JoinRequestResponseDTO> createRequest(@RequestBody JoinRequestDTO request) {
        return new ResponseEntity<>(poolRequestService.createRequest(request), HttpStatus.CREATED);
    }

    @GetMapping("/listing/{listingId}")
    public ResponseEntity<List<JoinRequestResponseDTO>> getRequestsByListing(@PathVariable Long listingId) {
        return ResponseEntity.ok(poolRequestService.getRequestsByListing(listingId));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<JoinRequestResponseDTO> updateStatus(
            @PathVariable Long id,
            @RequestParam RequestStatus status) {
        return ResponseEntity.ok(poolRequestService.updateStatus(id, status));
    }

    @GetMapping("/owner/{ownerId}/pending")
    public ResponseEntity<List<JoinRequestResponseDTO>> getPendingRequests(@PathVariable Long ownerId) {
        return ResponseEntity.ok(poolRequestService.getPendingRequestsForOwner(ownerId));
    }
}