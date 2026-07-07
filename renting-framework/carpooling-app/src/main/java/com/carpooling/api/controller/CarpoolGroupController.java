package com.carpooling.api.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.carpooling.api.dto.CarpoolGroupResponseDTO;
import com.carpooling.api.service.CarpoolGroupService;
import com.rentingframework.core.dto.MessageResponseDTO;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/carpool-groups")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class CarpoolGroupController {

    private final CarpoolGroupService carpoolGroupService;

    @GetMapping("/vehicle/{listingId}")
    public ResponseEntity<CarpoolGroupResponseDTO> getByVehicle(@PathVariable Long listingId) {
        return ResponseEntity.ok(carpoolGroupService.getGroupDtoByListingId(listingId));
    }

    // Plural on purpose - a driver can genuinely belong to more than one
    // pool at once, unlike Housing's "at most one house" assumption.
    @GetMapping("/driver/{driverId}")
    public ResponseEntity<List<CarpoolGroupResponseDTO>> getByDriver(@PathVariable Long driverId) {
        return ResponseEntity.ok(carpoolGroupService.getAllGroupsForDriver(driverId));
    }

    @PutMapping("/{groupId}/usage-policy")
    public ResponseEntity<Void> updateUsagePolicy(@PathVariable Long groupId, @RequestBody Map<String, String> body) {
        carpoolGroupService.updateUsagePolicy(groupId, body.get("usagePolicy"));
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{groupId}/messages")
    public ResponseEntity<MessageResponseDTO> sendMessage(
            @PathVariable Long groupId,
            @RequestParam Long senderId,
            @RequestBody Map<String, String> body) {
        MessageResponseDTO response = carpoolGroupService.sendMessage(groupId, senderId, body.get("content"));
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{groupId}/messages")
    public ResponseEntity<List<MessageResponseDTO>> getMessages(@PathVariable Long groupId) {
        return ResponseEntity.ok(carpoolGroupService.getMessages(groupId));
    }
}