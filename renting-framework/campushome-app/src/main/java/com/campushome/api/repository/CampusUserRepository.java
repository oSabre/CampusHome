package com.campushome.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.campushome.api.model.CampusUser;

/**
 * Sits alongside core's UserRepository, not instead of it. Core's version
 * is what registerUser()/login()/findById() search against for the
 * generic pipeline (email uniqueness, auth), but those methods return the
 * base User type — casting to read course/cpfCnpj/role every time gets
 * old fast. This is also what updateBio() uses to save a plain field
 * change without running through registerUser()'s email-uniqueness check,
 * which would wrongly flag an existing user's own email as taken.
 */
public interface CampusUserRepository extends JpaRepository<CampusUser, Long> {
}