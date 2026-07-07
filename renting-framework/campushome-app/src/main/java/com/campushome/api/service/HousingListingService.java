package com.campushome.api.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.campushome.api.dto.HousingListingRequestDTO;
import com.campushome.api.dto.HousingListingResponseDTO;
import com.campushome.api.model.CampusUser;
import com.campushome.api.model.HousingGroup;
import com.campushome.api.model.HousingListing;
import com.campushome.api.repository.HousingListingRepository;
import com.rentingframework.core.exception.ResourceNotFoundException;
import com.rentingframework.core.model.User;
import com.rentingframework.core.repository.GroupRepository;
import com.rentingframework.core.service.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class HousingListingService {

    private final HousingListingRepository housingListingRepository;
    private final UserService userService;
    private final GroupRepository groupRepository;

    public HousingListingResponseDTO publish(HousingListingRequestDTO request) {
        User ownerBase = userService.findById(request.getUserId());
        CampusUser owner = (CampusUser) ownerBase;

        // No more "only OWNER can publish" check — core's self-request
        // guard in RequestService already blocks the one real invariant
        // (requesting your own listing), so this restriction wasn't
        // protecting anything beyond that.

        HousingListing listing = new HousingListing();
        listing.setTitle(request.getTitle());
        listing.setDescription(request.getDescription());
        listing.setPrice(request.getPrice());
        listing.setNeighborhood(request.getNeighborhood());
        listing.setAddress(request.getAddress());
        listing.setOwner(owner);

        HousingListing saved = housingListingRepository.save(listing);

        // Pre-create the group as a HousingGroup right here, at publish
        // time — not lazily on first accept. Core's
        // GroupService.getOrCreateGroupForListing() can only ever fall
        // back to a plain Group if none exists yet, since core has no
        // knowledge of HousingGroup; that plain Group then fails every
        // later (HousingGroup) cast. Creating it here, with the concrete
        // subclass, means core's fallback never has a reason to fire.
        HousingGroup group = new HousingGroup();
        group.setListing(saved);
        group.setRules("Bem-vindos! Respeitem o espaço.");
        groupRepository.save(group);

        return convertToResponseDTO(saved);
    }

    public List<HousingListingResponseDTO> searchAds(String neighborhood, BigDecimal maxPrice) {
        List<HousingListing> ads;

        if (neighborhood != null && maxPrice != null) {
            ads = housingListingRepository.findByActiveTrueAndNeighborhoodAndPriceLessThanEqual(neighborhood, maxPrice);
        } else if (neighborhood != null) {
            ads = housingListingRepository.findByActiveTrueAndNeighborhood(neighborhood);
        } else if (maxPrice != null) {
            ads = housingListingRepository.findByActiveTrueAndPriceLessThanEqual(maxPrice);
        } else {
            ads = housingListingRepository.findByActiveTrue();
        }

        return ads.stream().map(this::convertToResponseDTO).collect(Collectors.toList());
    }

    public void toggleAdStatus(Long adId, Long ownerId) {
        HousingListing ad = housingListingRepository.findById(adId)
                .orElseThrow(() -> new ResourceNotFoundException("Anúncio não encontrado."));

        // This one stays — it's an ownership check, not a role check.
        if (!ad.getOwner().getId().equals(ownerId)) {
            throw new RuntimeException("Você não tem permissão para alterar este anúncio.");
        }

        ad.setActive(!ad.isActive());
        housingListingRepository.save(ad);
    }

    public List<HousingListingResponseDTO> getAdsByOwner(Long ownerId) {
        return housingListingRepository.findByOwnerId(ownerId).stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    private HousingListingResponseDTO convertToResponseDTO(HousingListing ad) {
        return new HousingListingResponseDTO(
            ad.getId(),
            ad.getTitle(),
            ad.getPrice(),
            ad.getNeighborhood(),
            ad.getOwner().getName(),
            ad.isActive()
        );
    }
}