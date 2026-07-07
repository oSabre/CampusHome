package com.expert.api.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.expert.api.model.Session;

public interface SessionRepository extends JpaRepository<Session, Long> {

    List<Session> findByClientId(Long clientId);

    List<Session> findByExpertListingIdOrderByStartTimeAsc(Long expertListingId);

    /**
     * The actual exclusivity rule for this app - and the one place it
     * genuinely differs from Booking's equivalent. Booking scopes its
     * overlap-check to a single vehicleListing.id, because the vehicle
     * itself is the exclusive resource. Here, the exclusive resource is
     * the SPECIALIST, who could have more than one ExpertListing (e.g.
     * "Resume Review" and "Mock Interview") but only one calendar - so
     * this scopes by s.expertListing.owner.id, checking across every
     * listing that specialist has posted, not just the one listing this
     * particular session request came in on.
     *
     * s.expertListing.owner is a valid JPQL path even though owner is
     * declared on core's Listing, not on ExpertListing directly - JOINED
     * inheritance means ExpertListing genuinely is a Listing, and
     * Hibernate resolves inherited properties in JPQL the same way it
     * would for any other entity property.
     */
    @Query("SELECT s FROM Session s WHERE s.expertListing.owner.id = :specialistId "
            + "AND s.status <> com.expert.api.enums.SessionStatus.CANCELLED "
            + "AND s.startTime < :endTime AND s.endTime > :startTime")
    List<Session> findOverlappingSessionsForSpecialist(
            @Param("specialistId") Long specialistId,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime);

}