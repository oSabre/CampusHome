package com.carpooling.api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.carpooling.api.service.CarpoolMatchingStrategy;
import com.rentingframework.core.dto.MatchResponseDTO;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/match")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class MatchController {

    private final CarpoolMatchingStrategy matchingStrategy;

    @GetMapping("/preview")
    public ResponseEntity<MatchResponseDTO> getMatchPreview(
            @RequestParam Long driverId,
            @RequestParam Long listingId) {
        return ResponseEntity.ok(matchingStrategy.calculateMatch(driverId, listingId));
    }
}