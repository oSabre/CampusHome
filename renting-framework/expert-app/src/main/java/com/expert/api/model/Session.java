package com.expert.api.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.expert.api.enums.SessionStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * No core equivalent, same as Booking - and structured the same way:
 * client (the actor/booker) + expertListing, with the specialist derived
 * via expertListing.getOwner() rather than stored as a separate field,
 * exactly like Booking derives the vehicle owner instead of duplicating it.
 *
 * The one real difference from Booking: the exclusive resource here is
 * the SPECIALIST, not the listing. A specialist could post more than one
 * ExpertListing (different services, same person's calendar), so the
 * eventual overlap-check query needs to scope by specialist across all
 * of their listings - not just by expertListing.id the way Booking's
 * overlap-check scopes by vehicleListing.id. That's a repository-layer
 * concern, not something this entity needs to encode, but it's the
 * reason client + expertListing are both here rather than a simpler
 * single foreign key.
 */
@Entity
@Table(name = "sessions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Session {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)
    private ExpertUser client;

    @ManyToOne
    @JoinColumn(name = "expert_listing_id", nullable = false)
    private ExpertListing expertListing;

    @Column(nullable = false)
    private LocalDateTime startTime;

    @Column(nullable = false)
    private LocalDateTime endTime;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SessionStatus status;

    @Column(columnDefinition = "TEXT")
    private String notes;

    private BigDecimal invoiceAmount;

    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        if (this.status == null) {
            this.status = SessionStatus.SCHEDULED;
        }
    }
}