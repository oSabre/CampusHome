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

import com.expert.api.dto.ExpertListingRequestDTO;
import com.expert.api.dto.ExpertListingResponseDTO;
import com.expert.api.service.ExpertListingService;

import lombok.RequiredArgsConstructor;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/expert-listings")
@RequiredArgsConstructor
public class ExpertListingController {

    private final ExpertListingService expertListingService;

    @PostMapping
    public ResponseEntity<ExpertListingResponseDTO> publish(@RequestBody ExpertListingRequestDTO request) {
        return new ResponseEntity<>(expertListingService.publish(request), HttpStatus.CREATED);
    }

    @GetMapping("/search")
    public ResponseEntity<List<ExpertListingResponseDTO>> search(
            @RequestParam(required = false) String specialty) {
        return ResponseEntity.ok(expertListingService.search(specialty));
    }

    @GetMapping("/owner/{ownerId}")
    public ResponseEntity<List<ExpertListingResponseDTO>> getByOwner(@PathVariable Long ownerId) {
        return ResponseEntity.ok(expertListingService.getByOwner(ownerId));
    }

    // specialistId, not callerId - specialist-only, like Housing's
    // owner-only check, unlike Carpooling's any-pool-member model.
    @PatchMapping("/{id}/toggle-status")
    public ResponseEntity<Void> toggleStatus(@PathVariable Long id, @RequestParam Long specialistId) {
        expertListingService.toggleStatus(id, specialistId);
        return ResponseEntity.noContent().build();
    }
}