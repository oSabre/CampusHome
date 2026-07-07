package com.campushome.api.service;

import java.util.stream.Collectors;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;

import com.campushome.api.model.CampusUser;
import com.rentingframework.core.dto.MatchResponseDTO;
import com.rentingframework.core.model.User;
import com.rentingframework.core.repository.GroupRepository;
import com.rentingframework.core.service.MatchingStrategy;
import com.rentingframework.core.service.UserService;

import lombok.RequiredArgsConstructor;

/**
 * The one required LLM-matching implementation for this app. GroupRepository
 * is injected directly (not through core's GroupService) because this needs
 * a read-only peek at whoever's already in the group for a listing — and
 * core's GroupService only exposes getOrCreateGroupForListing(), which
 * would create an empty group as a side effect just to preview a match
 * before any request has been accepted yet.
 */
@Service
@RequiredArgsConstructor
public class HousingMatchingStrategy implements MatchingStrategy {

    private final ChatClient chatClient;
    private final UserService userService;
    private final GroupRepository groupRepository;

    @Override
    public MatchResponseDTO calculateMatch(Long requesterId, Long listingId) {
        User requesterBase = userService.findById(requesterId);
        // Safe here: every User row in this app is a CampusUser — there's
        // no other subtype sharing this app's table.
        CampusUser requester = (CampusUser) requesterBase;

        String residentsBios = groupRepository.findByListingId(listingId)
                .map(group -> group.getMembers().stream()
                        .map(member -> {
                            CampusUser resident = (CampusUser) member;
                            return "Curso: " + resident.getCourse() + ", Bio: " + resident.getBio();
                        })
                        .collect(Collectors.joining(" | ")))
                .orElse("Ainda não há moradores nesta república.");

        String userPrompt = String.format(
            "Analise a compatibilidade entre um novo estudante e os moradores atuais de uma república.\n" +
            "Novo Estudante: Curso: %s, Bio: %s\n" +
            "Moradores Atuais: %s\n" +
            "Retorne um score de 0 a 100 e uma justificativa curta.",
            requester.getCourse(), requester.getBio(), residentsBios
        );

        return chatClient.prompt()
                .user(userPrompt)
                .call()
                .entity(new ParameterizedTypeReference<MatchResponseDTO>() {});
    }
}