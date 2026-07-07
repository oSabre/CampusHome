package com.campushome.api.controller;

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

import com.campushome.api.dto.HousingGroupResponseDTO;
import com.campushome.api.service.HousingGroupService;
import com.rentingframework.core.dto.MessageResponseDTO;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/housing-groups")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class HousingGroupController {

    private final HousingGroupService housingGroupService;

    @GetMapping("/advertisement/{adId}")
    public ResponseEntity<HousingGroupResponseDTO> getByAdvertisementId(@PathVariable Long adId) {
        return ResponseEntity.ok(housingGroupService.getGroupDtoByListingId(adId));
    }

    @PutMapping("/{id}/rules")
    public ResponseEntity<Void> updateRules(@PathVariable Long id, @RequestBody Map<String, String> body) {
        housingGroupService.updateRules(id, body.get("rules"));
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<HousingGroupResponseDTO> getByStudentId(@PathVariable Long studentId) {
        HousingGroupResponseDTO response = housingGroupService.getGroupDtoByStudentId(studentId);

        if (response == null) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(response);
    }

    @PostMapping("/{groupId}/messages")
    public ResponseEntity<MessageResponseDTO> sendMessage(
            @PathVariable Long groupId,
            @RequestParam Long senderId,
            @RequestBody Map<String, String> body) {
        MessageResponseDTO response = housingGroupService.sendMessage(groupId, senderId, body.get("content"));
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{groupId}/messages")
    public ResponseEntity<List<MessageResponseDTO>> getMessages(@PathVariable Long groupId) {
        return ResponseEntity.ok(housingGroupService.getMessages(groupId));
    }
}