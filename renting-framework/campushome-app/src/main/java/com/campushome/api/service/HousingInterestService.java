package com.campushome.api.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.campushome.api.dto.InterestRequestDTO;
import com.campushome.api.dto.InterestResponseDTO;
import com.rentingframework.core.dto.MatchResponseDTO;
import com.rentingframework.core.enums.RequestStatus;
import com.rentingframework.core.model.Request;
import com.rentingframework.core.repository.RequestRepository;
import com.rentingframework.core.service.RequestService;

import lombok.RequiredArgsConstructor;

/**
 * RequestRepository is injected directly for findByListingId() — core's
 * RequestService doesn't wrap a "list all requests for this listing"
 * read, only the accept/reject/create/pending-for-owner operations. Same
 * pattern core itself already uses (RequestService injects
 * ListingRepository directly for cases its own dependencies don't cover).
 */
@Service
@RequiredArgsConstructor
public class HousingInterestService {

    private final RequestService requestService;
    private final RequestRepository requestRepository;
    private final HousingMatchingStrategy matchingStrategy;

    public InterestResponseDTO createInterest(InterestRequestDTO request) {
        Request saved = requestService.createRequest(request.getStudentId(), request.getAdId());
        return convertToResponseDTO(saved);
    }

    public List<InterestResponseDTO> getInterestsByAd(Long adId) {
        return requestRepository.findByListingId(adId).stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    public InterestResponseDTO updateStatus(Long interestId, RequestStatus newStatus) {
        Request updated;

        if (newStatus == RequestStatus.ACCEPTED) {
            updated = requestService.acceptRequest(interestId);
        } else if (newStatus == RequestStatus.REJECTED) {
            updated = requestService.rejectRequest(interestId);
        } else {
            throw new RuntimeException("Transição de status não suportada.");
        }

        return convertToResponseDTO(updated);
    }

    public List<InterestResponseDTO> getPendingInterestsForOwner(Long ownerId) {
        return requestService.getPendingRequestsForOwner(ownerId).stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    private InterestResponseDTO convertToResponseDTO(Request request) {
        MatchResponseDTO match = matchingStrategy.calculateMatch(
                request.getRequester().getId(), request.getListing().getId());
        return new InterestResponseDTO(request, match.getScore(), match.getJustification());
    }
}