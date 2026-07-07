package com.expert.api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.expert.api.model.ExpertListing;

public interface ExpertListingRepository extends JpaRepository<ExpertListing, Long> {

    List<ExpertListing> findByActiveTrue();

    List<ExpertListing> findByActiveTrueAndSpecialty(String specialty);

    List<ExpertListing> findByOwnerId(Long ownerId);

}