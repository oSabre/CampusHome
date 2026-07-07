package com.expert.api.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.expert.api.dto.EngagementRequestDTO;
import com.expert.api.dto.EngagementResponseDTO;
import com.expert.api.model.ExpertListing;
import com.expert.api.repository.ExpertListingRepository;
import com.rentingframework.core.dto.MatchResponseDTO;
import com.rentingframework.core.enums.RequestStatus;
import com.rentingframework.core.exception.ResourceNotFoundException;
import com.rentingframework.core.model.Request;
import com.rentingframework.core.repository.RequestRepository;
import com.rentingframework.core.service.RequestService;

import lombok.RequiredArgsConstructor;

/**
 * The one rule neither HousingInterestService nor PoolRequestService
 * needed: each ExpertListing is single-use. Accepting one client's
 * engagement immediately deactivates the listing, so a second client can
 * never be accepted against it - which is what keeps this app's Group
 * always exactly 2 members instead of growing the way Housing's or
 * Carpooling's legitimately do.
 */
@Service
@RequiredArgsConstructor
public class ExpertEngagementService {

    private final RequestService requestService;
    private final RequestRepository requestRepository;
    private final ExpertListingRepository expertListingRepository;
    private final ExpertMatchingStrategy matchingStrategy;

    @Transactional
    public EngagementResponseDTO createEngagementRequest(EngagementRequestDTO request) {
        ExpertListing listing = expertListingRepository.findById(request.getListingId())
                .orElseThrow(() -> new ResourceNotFoundException("Anúncio não encontrado."));

        if (!listing.isActive()) {
            throw new RuntimeException("Este especialista já está comprometido com outro cliente para este anúncio.");
        }

        Request saved = requestService.createRequest(request.getClientId(), request.getListingId());
        return convertToResponseDTO(saved);
    }

    public List<EngagementResponseDTO> getRequestsByListing(Long listingId) {
        return requestRepository.findByListingId(listingId).stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public EngagementResponseDTO updateStatus(Long requestId, RequestStatus newStatus) {
        Request updated;

        if (newStatus == RequestStatus.ACCEPTED) {
            updated = requestService.acceptRequest(requestId);

            // The single-use-slot enforcement: deactivate the listing the
            // moment a client is accepted, so it can never be requested
            // or accepted again.
            ExpertListing listing = (ExpertListing) updated.getListing();
            listing.setActive(false);
            expertListingRepository.save(listing);
        } else if (newStatus == RequestStatus.REJECTED) {
            updated = requestService.rejectRequest(requestId);
        } else {
            throw new RuntimeException("Transição de status não suportada.");
        }

        return convertToResponseDTO(updated);
    }

    public List<EngagementResponseDTO> getPendingRequestsForOwner(Long ownerId) {
        return requestService.getPendingRequestsForOwner(ownerId).stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    private EngagementResponseDTO convertToResponseDTO(Request request) {
        MatchResponseDTO match = matchingStrategy.calculateMatch(
                request.getRequester().getId(), request.getListing().getId());
        return new EngagementResponseDTO(request, match.getScore(), match.getJustification());
    }
}