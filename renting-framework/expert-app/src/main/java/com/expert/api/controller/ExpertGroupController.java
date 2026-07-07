package com.expert.api.controller;

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

import com.expert.api.dto.ExpertGroupResponseDTO;
import com.expert.api.service.ExpertGroupService;
import com.rentingframework.core.dto.MessageResponseDTO;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/expert-groups")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class ExpertGroupController {

    private final ExpertGroupService expertGroupService;

    @GetMapping("/listing/{listingId}")
    public ResponseEntity<ExpertGroupResponseDTO> getByListing(@PathVariable Long listingId) {
        return ResponseEntity.ok(expertGroupService.getGroupDtoByListingId(listingId));
    }

    // Plural - a client can genuinely engage more than one specialist.
    @GetMapping("/client/{clientId}")
    public ResponseEntity<List<ExpertGroupResponseDTO>> getByClient(@PathVariable Long clientId) {
        return ResponseEntity.ok(expertGroupService.getAllGroupsForClient(clientId));
    }

    @PutMapping("/{groupId}/engagement-notes")
    public ResponseEntity<Void> updateEngagementNotes(@PathVariable Long groupId, @RequestBody Map<String, String> body) {
        expertGroupService.updateEngagementNotes(groupId, body.get("engagementNotes"));
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{groupId}/messages")
    public ResponseEntity<MessageResponseDTO> sendMessage(
            @PathVariable Long groupId,
            @RequestParam Long senderId,
            @RequestBody Map<String, String> body) {
        MessageResponseDTO response = expertGroupService.sendMessage(groupId, senderId, body.get("content"));
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{groupId}/messages")
    public ResponseEntity<List<MessageResponseDTO>> getMessages(@PathVariable Long groupId) {
        return ResponseEntity.ok(expertGroupService.getMessages(groupId));
    }
}