package com.rentingframework.core.model;

import java.time.LocalDateTime;
 
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
 
/**
 * The thing being posted — a room, a shared car, a specialist's slot.
 * Deliberately thin: no price field here, since pricing shape differs
 * enough across apps (flat rent vs. hourly rate vs. usage-based cost
 * split) that forcing one field on it would be a false shared point.
 * Each app adds its own pricing/attribute fields on the subclass.
 *
 *   @Entity
 *   @Table(name = "housing_listings")
 *   public class HousingListing extends Listing {
 *       private BigDecimal price;
 *       private String neighborhood;
 *       private String address;
 *   }
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "listings")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Listing {
 
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
 
    @Column(nullable = false)
    private String title;
 
    @Column(columnDefinition = "TEXT", nullable = false)
    private String description;
 
    private boolean active = true;
 
    private LocalDateTime createdAt;
 
    @ManyToOne
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;
 
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}