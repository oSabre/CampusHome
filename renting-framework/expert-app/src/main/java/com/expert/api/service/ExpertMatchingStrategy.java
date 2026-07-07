package com.expert.api.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;

import com.expert.api.model.ExpertListing;
import com.expert.api.model.ExpertUser;
import com.expert.api.repository.ExpertListingRepository;
import com.rentingframework.core.dto.MatchResponseDTO;
import com.rentingframework.core.model.User;
import com.rentingframework.core.service.MatchingStrategy;
import com.rentingframework.core.service.UserService;

import lombok.RequiredArgsConstructor;

/**
 * Simpler than HousingMatchingStrategy/CarpoolMatchingStrategy - no
 * GroupRepository needed here at all. Housing and Carpooling both had to
 * peek at "who else is already in the group" because their Groups are
 * genuinely collective (N residents, a driver pool). Expert's engagement
 * is 1:1, so matching is purely client-vs-specialist: no "current
 * members" context to describe.
 */
@Service
@RequiredArgsConstructor
public class ExpertMatchingStrategy implements MatchingStrategy {

    private final ChatClient chatClient;
    private final UserService userService;
    private final ExpertListingRepository expertListingRepository;

    @Override
    public MatchResponseDTO calculateMatch(Long requesterId, Long listingId) {
        User clientBase = userService.findById(requesterId);
        ExpertUser client = (ExpertUser) clientBase;

        ExpertListing listing = expertListingRepository.findById(listingId)
                .orElseThrow(() -> new RuntimeException("Anúncio não encontrado."));
        ExpertUser specialist = (ExpertUser) listing.getOwner();

        String userPrompt = String.format(
            "Analise a compatibilidade entre um cliente e um especialista.\n" +
            "Necessidade declarada pelo cliente: %s\n" +
            "Especialidade oferecida: %s\n" +
            "Especialista: %s (credenciais: %s)\n" +
            "Avalie o quão bem a necessidade do cliente se encaixa na especialidade oferecida.\n" +
            "Retorne um score de 0 a 100 e uma justificativa curta.",
            client.getBio(),
            listing.getSpecialty(),
            specialist.getSpecialty(),
            specialist.getCredentials()
        );

        return chatClient.prompt()
                .user(userPrompt)
                .call()
                .entity(new ParameterizedTypeReference<MatchResponseDTO>() {});
    }
}