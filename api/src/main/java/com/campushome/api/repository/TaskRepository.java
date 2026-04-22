package com.campushome.api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.campushome.api.model.Task;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByHousingGroupAdvertisementId(Long adId);
    
    List<Task> findByHousingGroupAdvertisementIdAndCompletedFalse(Long adId);
}
