package com.expert.api.model;

import java.math.BigDecimal;

import com.rentingframework.core.model.Listing;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * First app that actually needs a real price field on the Listing itself -
 * Housing deliberately left it off core's Listing (too varied across
 * domains), and this is exactly the domain where a flat rate belongs:
 * billing here is time-based (hourlyRate x session duration), not a flat
 * rent or a usage-split cost like the other two apps.
 *
 * specialty here is what THIS specific offering covers (e.g. "Resume
 * Review"), which can differ from the specialist's general specialty on
 * ExpertUser - a specialist might post more than one listing for
 * different services.
 */
@Entity
@Table(name = "expert_listings")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ExpertListing extends Listing {

    @Column(nullable = false)
    private String specialty;

    @Column(nullable = false)
    private BigDecimal hourlyRate;
}