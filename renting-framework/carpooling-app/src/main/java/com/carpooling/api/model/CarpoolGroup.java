package com.carpooling.api.model;

import com.rentingframework.core.model.Group;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "carpool_groups")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CarpoolGroup extends Group {

    @Column(columnDefinition = "TEXT")
    private String usagePolicy;
}