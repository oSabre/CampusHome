package com.campushome.api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.campushome.api.dto.InterestRequestDTO;
import com.campushome.api.dto.InterestResponseDTO;
import com.campushome.api.service.InterestService;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/interests")
public class InterestController {
   
    @Autowired
    private InterestService interestService;

    @PostMapping
    public ResponseEntity<InterestResponseDTO> createInterest(@RequestBody InterestRequestDTO request) {
        InterestResponseDTO response = interestService.createInterest(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/ad/{adId}")
    public ResponseEntity<List<InterestResponseDTO>> getInterestsByAd(@PathVariable Long adId) {
        List<InterestResponseDTO> response = interestService.getInterestsByAd(adId);
        return ResponseEntity.ok(response);
    }
}
