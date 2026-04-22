package com.campushome.api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.campushome.api.model.Interest;

public interface InterestRepository extends JpaRepository<Interest, Long>{
    List<Interest> findByAdvertisementId(Long adId);

    List<Interest> findByStudentId(Long studentId);

    boolean existsByStudentIdAndAdvertisementId(Long studentId, Long adId);
}
