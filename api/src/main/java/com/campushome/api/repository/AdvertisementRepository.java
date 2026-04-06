package com.campushome.api.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.campushome.api.model.Advertisement;



public interface AdvertisementRepository extends JpaRepository<Advertisement, Long>{
    List<Advertisement> findByNeighborhood(String neighborhood);

    List<Advertisement> findByPriceLessThanEqual(BigDecimal price);

    List<Advertisement> findByNeighborhoodAndPriceLessThanEqual(String neighborhood, BigDecimal price);

    List<Advertisement> findByTitleContainingIgnoreCase(String title);


}
