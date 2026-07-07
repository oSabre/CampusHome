package com.campushome.api.model;

import com.campushome.api.enums.UserRole;
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
 * CampusHome's concrete User. JOINED inheritance means this maps to its
 * own "campushome_users" table, joined to core's "users" table on the
 * same id — Hibernate handles the join transparently, including through
 * every core repository/service that works against the base User type.
 */
@Entity
@Table(name = "campushome_users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CampusUser extends User {

    // Apenas para estudantes
    private String course;

    // Apenas para donos
    private String cpfCnpj;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role;
}