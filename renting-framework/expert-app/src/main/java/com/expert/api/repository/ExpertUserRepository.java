package com.expert.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.expert.api.model.ExpertUser;

/**
 * Sits alongside core's UserRepository, not instead of it - same pattern
 * as CarUserRepository/CampusUserRepository. Typed access to role/
 * specialty/credentials without casting, and plain field updates that
 * shouldn't run through registerUser()'s email-uniqueness check.
 */
public interface ExpertUserRepository extends JpaRepository<ExpertUser, Long> {
}