package com.campushome.api.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.campushome.api.dto.MatchResponseDTO;
import com.campushome.api.service.MatchService;

@RestController
@RequestMapping("/match")
@CrossOrigin(origins = "*")
public class MatchController {
    private final MatchService matchService;

    public MatchController(MatchService matchService) {
        this.matchService = matchService;
    }

    @GetMapping("/preview")
    public ResponseEntity<MatchResponseDTO> getMatchPreview(
            @RequestParam Long studentId, 
            @RequestParam Long adId) {
        
        MatchResponseDTO response = matchService.calculateMatch(studentId, adId);
        return ResponseEntity.ok(response);
    }
}
