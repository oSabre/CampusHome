package com.rentingframework.core.repository;
 
import java.util.List;
 
import org.springframework.data.jpa.repository.JpaRepository;
 
import com.rentingframework.core.model.Listing;
 
/**
 * Deliberately minimal — only what RequestService and GroupService need
 * generically. Domain search (by neighborhood, by route, by specialty,
 * price ranges, etc.) belongs in each app's own repository, targeting
 * its own Listing subclass directly, e.g.:
 *
 *   public interface HousingListingRepository
 *           extends JpaRepository<HousingListing, Long> {
 *       List<HousingListing> findByActiveTrueAndNeighborhood(String n);
 *   }
 *
 * That repository is a separate Spring Data bean from this one, even
 * though both ultimately read/write the "listings" table (JOINED
 * inheritance) — Hibernate handles the join transparently either way.
 */
public interface ListingRepository extends JpaRepository<Listing, Long> {
 
    List<Listing> findByActiveTrue();
 
    List<Listing> findByOwnerId(Long ownerId);
 
}