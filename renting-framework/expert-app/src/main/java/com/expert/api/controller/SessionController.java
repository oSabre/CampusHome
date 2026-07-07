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
import org.springframework.web.bind.annotation.RestController;

import com.expert.api.dto.SessionCompletionDTO;
import com.expert.api.dto.SessionRequestDTO;
import com.expert.api.dto.SessionResponseDTO;
import com.expert.api.service.SessionService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/sessions")
@RequiredArgsConstructor
public class SessionController {

    private final SessionService sessionService;

    @PostMapping
    public ResponseEntity<SessionResponseDTO> createSession(@Valid @RequestBody SessionRequestDTO request) {
        return new ResponseEntity<>(sessionService.createSession(request), HttpStatus.CREATED);
    }

    @PatchMapping("/{id}/complete")
    public ResponseEntity<SessionResponseDTO> completeSession(
            @PathVariable Long id,
            @RequestBody SessionCompletionDTO request) {
        return ResponseEntity.ok(sessionService.completeSession(id, request.getNotes()));
    }

    @PatchMapping("/{id}/cancel")
    public ResponseEntity<Void> cancelSession(@PathVariable Long id) {
        sessionService.cancelSession(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/listing/{listingId}")
    public ResponseEntity<List<SessionResponseDTO>> listByListing(@PathVariable Long listingId) {
        return ResponseEntity.ok(sessionService.listByListing(listingId));
    }

    @GetMapping("/client/{clientId}")
    public ResponseEntity<List<SessionResponseDTO>> listByClient(@PathVariable Long clientId) {
        return ResponseEntity.ok(sessionService.listByClient(clientId));
    }
}