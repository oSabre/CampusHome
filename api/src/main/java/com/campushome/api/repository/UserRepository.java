package com.campushome.api.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.campushome.api.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}
