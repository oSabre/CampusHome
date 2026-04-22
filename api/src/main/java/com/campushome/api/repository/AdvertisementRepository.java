package com.campushome.api.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.campushome.api.model.Advertisement;



public interface AdvertisementRepository extends JpaRepository<Advertisement, Long>{
    List<Advertisement> findByActiveTrueAndNeighborhood(String neighborhood);

    List<Advertisement> findByActiveTrueAndPriceLessThanEqual(BigDecimal price);

    List<Advertisement> findByActiveTrueAndNeighborhoodAndPriceLessThanEqual(String neighborhood, BigDecimal price);

    List<Advertisement> findByActiveTrueAndTitleContainingIgnoreCase(String title);

    List<Advertisement> findByActiveTrue();


}
