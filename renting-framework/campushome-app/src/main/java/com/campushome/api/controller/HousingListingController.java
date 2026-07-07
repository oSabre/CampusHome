package com.campushome.api.controller;

import java.math.BigDecimal;
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

import com.campushome.api.dto.HousingListingRequestDTO;
import com.campushome.api.dto.HousingListingResponseDTO;
import com.campushome.api.service.HousingListingService;

import lombok.RequiredArgsConstructor;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/ads")
@RequiredArgsConstructor
public class HousingListingController {

    private final HousingListingService housingListingService;

    @PostMapping
    public ResponseEntity<HousingListingResponseDTO> publish(@RequestBody HousingListingRequestDTO request) {
        return new ResponseEntity<>(housingListingService.publish(request), HttpStatus.CREATED);
    }

    @GetMapping("/search")
    public ResponseEntity<List<HousingListingResponseDTO>> search(
            @RequestParam(required = false) String neighborhood,
            @RequestParam(required = false) BigDecimal maxPrice) {
        return ResponseEntity.ok(housingListingService.searchAds(neighborhood, maxPrice));
    }

    @PatchMapping("/{id}/toggle-status")
    public ResponseEntity<Void> toggleStatus(@PathVariable Long id, @RequestParam Long ownerId) {
        housingListingService.toggleAdStatus(id, ownerId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/owner/{ownerId}")
    public ResponseEntity<List<HousingListingResponseDTO>> getByOwner(@PathVariable Long ownerId) {
        return ResponseEntity.ok(housingListingService.getAdsByOwner(ownerId));
    }
}