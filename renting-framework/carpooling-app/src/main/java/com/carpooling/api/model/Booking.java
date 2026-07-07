package com.carpooling.api.model;

import java.time.LocalDateTime;

import com.carpooling.api.enums.BookingStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * No core equivalent — booking/concurrency was scoped out of core as a
 * variable point from the start. Because of that, this can type its
 * relationships directly against CarUser/VehicleListing instead of core's
 * base User/Listing, so nothing here ever needs a cast-to-subclass, unlike
 * almost everything in HousingGroupService/HousingMatchingStrategy.
 *
 * The actual exclusivity rule ("one driver at a time") isn't enforced by
 * this entity itself — it's a query in BookingRepository checking for
 * overlapping time ranges on the same vehicleListing, run before a new
 * Booking is allowed to save.
 */
@Entity
@Table(name = "bookings")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "driver_id", nullable = false)
    private CarUser driver;

    @ManyToOne
    @JoinColumn(name = "vehicle_listing_id", nullable = false)
    private VehicleListing vehicleListing;

    @Column(nullable = false)
    private LocalDateTime startTime;

    @Column(nullable = false)
    private LocalDateTime endTime;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BookingStatus status;

    private Integer odometerStart;

    private Integer odometerEnd;

    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        if (this.status == null) {
            this.status = BookingStatus.SCHEDULED;
        }
    }
}