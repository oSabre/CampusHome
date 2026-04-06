package com.campushome.api.controller;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.campushome.api.dto.AdRequestDTO;
import com.campushome.api.dto.AdResponseDTO;
import com.campushome.api.service.AdService;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/ads")
public class AdController {
    
    @Autowired
    private AdService adService;

    @PostMapping
    public ResponseEntity<AdResponseDTO> publish(@RequestBody AdRequestDTO request) {
        AdResponseDTO response = adService.publish(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/search")
    public ResponseEntity<List<AdResponseDTO>> search(
            @RequestParam(required = false) String neighborhood,
            @RequestParam(required = false) BigDecimal maxPrice) {
        
        List<AdResponseDTO> response = adService.searchAds(neighborhood, maxPrice);
        return ResponseEntity.ok(response);
    }
    
    
}
