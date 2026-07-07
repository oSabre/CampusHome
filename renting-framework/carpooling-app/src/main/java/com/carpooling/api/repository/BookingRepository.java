package com.carpooling.api.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.carpooling.api.model.Booking;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findByVehicleListingIdOrderByStartTimeAsc(Long vehicleListingId);

    List<Booking> findByDriverId(Long driverId);

    /**
     * The actual "one driver at a time" rule. Two time ranges [startA, endA)
     * and [startB, endB) overlap exactly when startA < endB AND endB >
     * startA — cancelled bookings don't block anything, since the slot is
     * effectively free again once one is cancelled. BookingService checks
     * this list is empty before allowing a new booking to save; it's not
     * enforced by the entity or a DB constraint, just a check the service
     * layer runs first.
     */
    @Query("SELECT b FROM Booking b WHERE b.vehicleListing.id = :vehicleListingId "
            + "AND b.status <> com.carpooling.api.enums.BookingStatus.CANCELLED "
            + "AND b.startTime < :endTime AND b.endTime > :startTime")
    List<Booking> findOverlappingBookings(
            @Param("vehicleListingId") Long vehicleListingId,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime);

}