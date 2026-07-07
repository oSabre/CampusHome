package com.campushome.api.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.campushome.api.model.HousingListing;

/**
 * Targets HousingListing directly (not core's Listing) because search
 * needs neighborhood/price — fields that only exist on this subclass.
 * Core's ListingRepository.findByActiveTrue()/findByOwnerId() still work
 * fine polymorphically, but they'd hand back plain Listing instances with
 * no access to price/neighborhood, which is no good for a search screen.
 */
public interface HousingListingRepository extends JpaRepository<HousingListing, Long> {

    List<HousingListing> findByActiveTrue();

    List<HousingListing> findByActiveTrueAndNeighborhood(String neighborhood);

    List<HousingListing> findByActiveTrueAndPriceLessThanEqual(BigDecimal maxPrice);

    List<HousingListing> findByActiveTrueAndNeighborhoodAndPriceLessThanEqual(String neighborhood, BigDecimal maxPrice);

    List<HousingListing> findByOwnerId(Long ownerId);

}