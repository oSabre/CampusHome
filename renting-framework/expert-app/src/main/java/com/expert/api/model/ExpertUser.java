package com.expert.api.model;

import com.expert.api.enums.UserRole;
import com.rentingframework.core.model.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Real role asymmetry again, like CampusHome's STUDENT/OWNER - unlike
 * Carpooling, which deliberately has none. specialty/credentials are
 * nullable and only meaningful for SPECIALIST, same "apenas para X"
 * pattern as CampusUser's course/cpfCnpj. Core's bio field is reused
 * generically here too - a client's stated need or a specialist's pitch,
 * same repurposing as Carpooling's availability-in-bio.
 */
@Entity
@Table(name = "expert_users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ExpertUser extends User {

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role;

    // Apenas para SPECIALIST
    private String specialty;

    // Apenas para SPECIALIST
    private String credentials;
}