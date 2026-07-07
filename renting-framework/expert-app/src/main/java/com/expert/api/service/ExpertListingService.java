package com.expert.api.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.expert.api.dto.ExpertListingRequestDTO;
import com.expert.api.dto.ExpertListingResponseDTO;
import com.expert.api.model.ExpertGroup;
import com.expert.api.model.ExpertListing;
import com.expert.api.model.ExpertUser;
import com.expert.api.repository.ExpertListingRepository;
import com.rentingframework.core.exception.ResourceNotFoundException;
import com.rentingframework.core.model.User;
import com.rentingframework.core.repository.GroupRepository;
import com.rentingframework.core.service.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ExpertListingService {

    private final ExpertListingRepository expertListingRepository;
    private final UserService userService;
    private final GroupRepository groupRepository;

    public ExpertListingResponseDTO publish(ExpertListingRequestDTO request) {
        User ownerBase = userService.findById(request.getUserId());
        ExpertUser specialist = (ExpertUser) ownerBase;

        ExpertListing listing = new ExpertListing();
        listing.setTitle(request.getTitle());
        listing.setDescription(request.getDescription());
        listing.setSpecialty(request.getSpecialty());
        listing.setHourlyRate(request.getHourlyRate());
        listing.setOwner(specialist);

        ExpertListing saved = expertListingRepository.save(listing);

        // Pre-create the group as an ExpertGroup (not core's generic
        // Group), same fix pattern as Housing/Carpooling - otherwise
        // core's lazy fallback creates a plain Group on first accept.
        // Unlike Carpooling, the specialist is NOT added as a member here
        // - Expert has real asymmetry (like Housing's landlord), so the
        // specialist only joins the group once a client's engagement is
        // actually accepted, via core's acceptRequest() adding both
        // listing.getOwner() and the requester automatically.
        ExpertGroup group = new ExpertGroup();
        group.setListing(saved);
        groupRepository.save(group);

        return convertToResponseDTO(saved);
    }

    public List<ExpertListingResponseDTO> search(String specialty) {
        List<ExpertListing> listings = (specialty != null)
                ? expertListingRepository.findByActiveTrueAndSpecialty(specialty)
                : expertListingRepository.findByActiveTrue();

        return listings.stream().map(this::convertToResponseDTO).collect(Collectors.toList());
    }

    public List<ExpertListingResponseDTO> getByOwner(Long ownerId) {
        return expertListingRepository.findByOwnerId(ownerId).stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Specialist-only, like Housing's owner-only check - not Carpooling's
     * pool-member model. There's no pool here; a listing belongs to
     * exactly one specialist until (at most) one client is accepted.
     */
    public void toggleStatus(Long listingId, Long callerId) {
        ExpertListing listing = expertListingRepository.findById(listingId)
                .orElseThrow(() -> new ResourceNotFoundException("Anúncio não encontrado."));

        if (!listing.getOwner().getId().equals(callerId)) {
            throw new RuntimeException("Apenas o especialista responsável pode alterar este anúncio.");
        }

        listing.setActive(!listing.isActive());
        expertListingRepository.save(listing);
    }

    private ExpertListingResponseDTO convertToResponseDTO(ExpertListing listing) {
        return new ExpertListingResponseDTO(
            listing.getId(),
            listing.getTitle(),
            listing.getSpecialty(),
            listing.getHourlyRate(),
            listing.getOwner().getName(),
            listing.isActive()
        );
    }
}