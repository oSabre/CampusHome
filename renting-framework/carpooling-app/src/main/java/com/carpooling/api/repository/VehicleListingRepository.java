package com.carpooling.api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.carpooling.api.model.VehicleListing;

/**
 * Kept modest on purpose, same as HousingListingRepository - only what's
 * actually needed right now. Add more derived queries here as real search
 * filters come up, rather than guessing ahead of the controller design.
 */
public interface VehicleListingRepository extends JpaRepository<VehicleListing, Long> {

    List<VehicleListing> findByActiveTrue();

    List<VehicleListing> findByActiveTrueAndMake(String make);

    List<VehicleListing> findByOwnerId(Long ownerId);

}