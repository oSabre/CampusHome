package com.campushome.api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.campushome.api.enums.InterestStatus;
import com.campushome.api.model.Interest;

public interface InterestRepository extends JpaRepository<Interest, Long>{
    List<Interest> findByAdvertisementId(Long adId);

    List<Interest> findByStudentId(Long studentId);

    boolean existsByStudentIdAndAdvertisementId(Long studentId, Long adId);

    @Query("SELECT i FROM Interest i WHERE i.advertisement.owner.id = :ownerId AND i.status = :status")
    List<Interest> findByOwnerIdAndStatus(Long ownerId, InterestStatus status);
}
