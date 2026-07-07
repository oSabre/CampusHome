package com.rentingframework.core.repository;
 
import java.util.List;
import java.util.Optional;
 
import org.springframework.data.jpa.repository.JpaRepository;
 
import com.rentingframework.core.model.Group;
 
public interface GroupRepository extends JpaRepository<Group, Long> {
 
    Optional<Group> findByListingId(Long listingId);
 
    // A user can belong to more than one group at once (e.g. two
    // different carpool pools), so this returns a list, not a single
    // Optional — unlike the old HousingGroupRepository.findByResidents_Id.
    List<Group> findByMembers_Id(Long userId);
 
}