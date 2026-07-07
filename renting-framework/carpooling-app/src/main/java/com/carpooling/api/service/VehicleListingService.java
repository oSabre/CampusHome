package com.carpooling.api.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.carpooling.api.dto.VehicleListingRequestDTO;
import com.carpooling.api.dto.VehicleListingResponseDTO;
import com.carpooling.api.model.CarUser;
import com.carpooling.api.model.CarpoolGroup;
import com.carpooling.api.model.VehicleListing;
import com.carpooling.api.repository.VehicleListingRepository;
import com.rentingframework.core.exception.ResourceNotFoundException;
import com.rentingframework.core.model.Group;
import com.rentingframework.core.model.User;
import com.rentingframework.core.repository.GroupRepository;
import com.rentingframework.core.service.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class VehicleListingService {

    private final VehicleListingRepository vehicleListingRepository;
    private final UserService userService;
    private final GroupRepository groupRepository;

    public VehicleListingResponseDTO publish(VehicleListingRequestDTO request) {
        User ownerBase = userService.findById(request.getUserId());
        CarUser owner = (CarUser) ownerBase;

        VehicleListing listing = new VehicleListing();
        listing.setTitle(request.getTitle());
        listing.setDescription(request.getDescription());
        listing.setMake(request.getMake());
        listing.setModel(request.getModel());
        listing.setYear(request.getYear());
        listing.setLicensePlate(request.getLicensePlate());
        listing.setMileage(request.getMileage());
        listing.setOwner(owner);

        VehicleListing saved = vehicleListingRepository.save(listing);

        // Pre-create the group as a CarpoolGroup (not core's generic
        // Group), same fix pattern as HousingListingService.publish() -
        // otherwise core's lazy fallback would create a plain Group on
        // first accept, breaking every later (CarpoolGroup) cast.
        //
        // Unlike Housing, the poster is added as a member right here,
        // not left for the accept-flow to add later - since this app has
        // no owner/renter asymmetry, whoever registers the car is already
        // a peer driver in the pool, not a landlord standing outside it.
        // Without this, toggleStatus()'s "any pool member" check would
        // lock the poster out of their own fresh listing until someone
        // else's request gets accepted.
        CarpoolGroup group = new CarpoolGroup();
        group.setListing(saved);
        group.setUsagePolicy("Registre a quilometragem e reabasteça o tanque após cada uso.");
        group.addMember(owner);
        groupRepository.save(group);

        return convertToResponseDTO(saved);
    }

    public List<VehicleListingResponseDTO> search(String make) {
        List<VehicleListing> listings = (make != null)
                ? vehicleListingRepository.findByActiveTrueAndMake(make)
                : vehicleListingRepository.findByActiveTrue();

        return listings.stream().map(this::convertToResponseDTO).collect(Collectors.toList());
    }

    public List<VehicleListingResponseDTO> getByOwner(Long ownerId) {
        return vehicleListingRepository.findByOwnerId(ownerId).stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Any current pool member can toggle, not just the original poster -
     * the deliberate permission-model difference from Housing's
     * owner-only check, agreed as a variable point rather than a mistake.
     */
    public void toggleStatus(Long listingId, Long callerId) {
        VehicleListing listing = vehicleListingRepository.findById(listingId)
                .orElseThrow(() -> new ResourceNotFoundException("Veículo não encontrado."));

        Group group = groupRepository.findByListingId(listingId)
                .orElseThrow(() -> new ResourceNotFoundException("Grupo não encontrado para este veículo."));

        boolean isMember = group.getMembers().stream().anyMatch(m -> m.getId().equals(callerId));
        if (!isMember) {
            throw new RuntimeException("Apenas participantes do grupo podem alterar o status deste veículo.");
        }

        listing.setActive(!listing.isActive());
        vehicleListingRepository.save(listing);
    }

    private VehicleListingResponseDTO convertToResponseDTO(VehicleListing listing) {
        return new VehicleListingResponseDTO(
            listing.getId(),
            listing.getTitle(),
            listing.getMake(),
            listing.getModel(),
            listing.getYear(),
            listing.getLicensePlate(),
            listing.getOwner().getName(),
            listing.isActive()
        );
    }
}