package com.rentingframework.core.repository;
 
import java.util.List;
 
import org.springframework.data.jpa.repository.JpaRepository;
 
import com.rentingframework.core.enums.RequestStatus;
import com.rentingframework.core.model.Request;
 
public interface RequestRepository extends JpaRepository<Request, Long> {
 
    List<Request> findByListingId(Long listingId);
 
    boolean existsByRequesterIdAndListingId(Long requesterId, Long listingId);
 
    // Used for "pending requests waiting on me" screens — the owner is
    // reached through the listing, since Request itself doesn't store it.
    List<Request> findByListing_Owner_IdAndStatus(Long ownerId, RequestStatus status);
 
}