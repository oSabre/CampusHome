package com.carpooling.api.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.carpooling.api.dto.JoinRequestDTO;
import com.carpooling.api.dto.JoinRequestResponseDTO;
import com.rentingframework.core.dto.MatchResponseDTO;
import com.rentingframework.core.enums.RequestStatus;
import com.rentingframework.core.model.Request;
import com.rentingframework.core.repository.RequestRepository;
import com.rentingframework.core.service.RequestService;

import lombok.RequiredArgsConstructor;

/**
 * Same shape as HousingInterestService - core's self-request guard,
 * duplicate-request guard, and PENDING-only accept/reject transitions all
 * apply automatically, nothing reimplemented here.
 */
@Service
@RequiredArgsConstructor
public class PoolRequestService {

    private final RequestService requestService;
    private final RequestRepository requestRepository;
    private final CarpoolMatchingStrategy matchingStrategy;

    public JoinRequestResponseDTO createRequest(JoinRequestDTO request) {
        Request saved = requestService.createRequest(request.getDriverId(), request.getListingId());
        return convertToResponseDTO(saved);
    }

    public List<JoinRequestResponseDTO> getRequestsByListing(Long listingId) {
        return requestRepository.findByListingId(listingId).stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    public JoinRequestResponseDTO updateStatus(Long requestId, RequestStatus newStatus) {
        Request updated;

        if (newStatus == RequestStatus.ACCEPTED) {
            updated = requestService.acceptRequest(requestId);
        } else if (newStatus == RequestStatus.REJECTED) {
            updated = requestService.rejectRequest(requestId);
        } else {
            throw new RuntimeException("Transição de status não suportada.");
        }

        return convertToResponseDTO(updated);
    }

    public List<JoinRequestResponseDTO> getPendingRequestsForOwner(Long ownerId) {
        return requestService.getPendingRequestsForOwner(ownerId).stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    private JoinRequestResponseDTO convertToResponseDTO(Request request) {
        MatchResponseDTO match = matchingStrategy.calculateMatch(
                request.getRequester().getId(), request.getListing().getId());
        return new JoinRequestResponseDTO(request, match.getScore(), match.getJustification());
    }
}