package com.carpooling.api.model;

import com.rentingframework.core.model.Listing;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * No price field, same reasoning as HousingListing: cost here is
 * usage-based (mileage/fuel split across whoever actually drove), not a
 * flat rate attached to the listing itself.
 */
@Entity
@Table(name = "vehicle_listings")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VehicleListing extends Listing {

    @Column(nullable = false)
    private String make;

    @Column(nullable = false)
    private String model;

    private Integer year;

    @Column(nullable = false, unique = true)
    private String licensePlate;

    private Integer mileage;
}