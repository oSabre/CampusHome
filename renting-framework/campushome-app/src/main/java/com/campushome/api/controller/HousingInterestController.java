package com.campushome.api.controller;

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

import com.campushome.api.dto.InterestRequestDTO;
import com.campushome.api.dto.InterestResponseDTO;
import com.campushome.api.service.HousingInterestService;
import com.rentingframework.core.enums.RequestStatus;

import lombok.RequiredArgsConstructor;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/interests")
@RequiredArgsConstructor
public class HousingInterestController {

    private final HousingInterestService housingInterestService;

    @PostMapping
    public ResponseEntity<InterestResponseDTO> createInterest(@RequestBody InterestRequestDTO request) {
        return new ResponseEntity<>(housingInterestService.createInterest(request), HttpStatus.CREATED);
    }

    @GetMapping("/ad/{adId}")
    public ResponseEntity<List<InterestResponseDTO>> getInterestsByAd(@PathVariable Long adId) {
        return ResponseEntity.ok(housingInterestService.getInterestsByAd(adId));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<InterestResponseDTO> updateInterestStatus(
            @PathVariable Long id,
            @RequestParam RequestStatus status) {
        return ResponseEntity.ok(housingInterestService.updateStatus(id, status));
    }

    @GetMapping("/owner/{ownerId}/pending")
    public ResponseEntity<List<InterestResponseDTO>> getPendingInterests(@PathVariable Long ownerId) {
        return ResponseEntity.ok(housingInterestService.getPendingInterestsForOwner(ownerId));
    }
}