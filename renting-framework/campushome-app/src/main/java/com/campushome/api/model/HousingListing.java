package com.campushome.api.model;

import java.math.BigDecimal;

import com.rentingframework.core.model.Listing;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "housing_listings")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HousingListing extends Listing {

    @Column(nullable = false)
    private BigDecimal price;

    private String neighborhood;

    private String address;
}