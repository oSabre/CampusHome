package com.rentingframework.core.model;

import java.time.LocalDateTime;
 
import com.rentingframework.core.enums.RequestStatus;
 
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
 * Someone asking in on a Listing (CampusHome's old Interest, generalized).
 * RequestService in core always: creates this as PENDING, and on
 * ACCEPTED, hands off to GroupService to form/update a Group. That flow
 * is fixed — what varies per app is what feeds the MatchingStrategy
 * score attached to a request, computed by the caller, not stored here.
 *
 *   @Entity
 *   @Table(name = "housing_requests")
 *   public class HousingRequest extends Request {
 *       // no extra fields needed for CampusHome — matching runs off
 *       // requester.bio / requester.course, already on User
 *   }
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "requests")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Request {
 
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
 
    @ManyToOne
    @JoinColumn(name = "requester_id", nullable = false)
    private User requester;
 
    @ManyToOne
    @JoinColumn(name = "listing_id", nullable = false)
    private Listing listing;
 
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RequestStatus status;
 
    private LocalDateTime createdAt;
 
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        if (this.status == null) {
            this.status = RequestStatus.PENDING;
        }
    }
}