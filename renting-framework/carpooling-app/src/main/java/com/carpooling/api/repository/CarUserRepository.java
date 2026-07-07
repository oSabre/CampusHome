package com.carpooling.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.carpooling.api.model.CarUser;

/**
 * Sits alongside core's UserRepository, not instead of it - same pattern
 * as CampusUserRepository in campushome-app. Core's registerUser()/login()/
 * findById() return the base User type; this is for typed access to
 * driverLicenseNumber/yearsLicensed without casting, and for plain field
 * updates that shouldn't run through registerUser()'s email-uniqueness
 * check.
 */
public interface CarUserRepository extends JpaRepository<CarUser, Long> {
}