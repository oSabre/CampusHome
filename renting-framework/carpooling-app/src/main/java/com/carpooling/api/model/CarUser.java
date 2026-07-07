package com.carpooling.api.model;

import com.rentingframework.core.model.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * No role field here — deliberately. Every driver can both post a shared
 * car and join someone else's pool; there's no owner/renter asymmetry to
 * encode, unlike CampusHome's STUDENT/OWNER split.
 */
@Entity
@Table(name = "carpooling_users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CarUser extends User {

    @Column(nullable = false)
    private String driverLicenseNumber;

    private Integer yearsLicensed;
}