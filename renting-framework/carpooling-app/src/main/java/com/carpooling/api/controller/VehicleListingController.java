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

import com.carpooling.api.dto.VehicleListingRequestDTO;
import com.carpooling.api.dto.VehicleListingResponseDTO;
import com.carpooling.api.service.VehicleListingService;

import lombok.RequiredArgsConstructor;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/vehicles")
@RequiredArgsConstructor
public class VehicleListingController {

    private final VehicleListingService vehicleListingService;

    @PostMapping
    public ResponseEntity<VehicleListingResponseDTO> publish(@RequestBody VehicleListingRequestDTO request) {
        return new ResponseEntity<>(vehicleListingService.publish(request), HttpStatus.CREATED);
    }

    @GetMapping("/search")
    public ResponseEntity<List<VehicleListingResponseDTO>> search(
            @RequestParam(required = false) String make) {
        return ResponseEntity.ok(vehicleListingService.search(make));
    }

    @GetMapping("/owner/{ownerId}")
    public ResponseEntity<List<VehicleListingResponseDTO>> getByOwner(@PathVariable Long ownerId) {
        return ResponseEntity.ok(vehicleListingService.getByOwner(ownerId));
    }

    // callerId, not ownerId - any current pool member can toggle, unlike
    // HousingListingController's owner-only check.
    @PatchMapping("/{id}/toggle-status")
    public ResponseEntity<Void> toggleStatus(@PathVariable Long id, @RequestParam Long callerId) {
        vehicleListingService.toggleStatus(id, callerId);
        return ResponseEntity.noContent().build();
    }
}