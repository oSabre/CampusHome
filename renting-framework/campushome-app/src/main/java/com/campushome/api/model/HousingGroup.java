package com.campushome.api.model;

import com.rentingframework.core.model.Group;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "housing_groups")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HousingGroup extends Group {

    @Column(columnDefinition = "TEXT")
    private String rules;
}