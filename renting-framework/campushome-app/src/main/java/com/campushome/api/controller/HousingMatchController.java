package com.campushome.api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.campushome.api.service.HousingMatchingStrategy;
import com.rentingframework.core.dto.MatchResponseDTO;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/match")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class HousingMatchController {

    private final HousingMatchingStrategy matchingStrategy;

    @GetMapping("/preview")
    public ResponseEntity<MatchResponseDTO> getMatchPreview(
            @RequestParam Long studentId,
            @RequestParam Long adId) {
        return ResponseEntity.ok(matchingStrategy.calculateMatch(studentId, adId));
    }
}