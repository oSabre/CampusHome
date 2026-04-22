package com.campushome.api.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.campushome.api.model.HousingGroup;

@Repository
public interface HousingGroupRepository extends JpaRepository<HousingGroup, Long> {
    Optional<HousingGroup> findByAdvertisementId(Long advertisementId);

    boolean existsByAdvertisementId(Long advertisementId);
}
