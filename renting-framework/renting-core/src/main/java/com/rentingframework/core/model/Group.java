package com.rentingframework.core.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
 
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
 
/**
 * The collaboration/communication unit formed when a Request is
 * accepted — CampusHome's HousingGroup, generalized. Works for N members
 * (a house, a car's pool of drivers) and for exactly 2 (a client and a
 * specialist) — Expert doesn't skip Group, it just always has 2 members.
 * Domain-specific content (house rules, car maintenance log, session
 * notes) belongs on the app's subclass, not here — this stays just
 * "who's in it" plus a place for Messages to attach to.
 *
 *   @Entity
 *   @Table(name = "housing_groups")
 *   public class HousingGroup extends Group {
 *       @Column(columnDefinition = "TEXT") private String rules;
 *   }
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "groups")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Group {
 
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
 
    @ManyToOne
    @JoinColumn(name = "listing_id")
    private Listing listing;
 
    @ManyToMany
    @JoinTable(
        name = "group_members",
        joinColumns = @JoinColumn(name = "group_id"),
        inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private List<User> members = new ArrayList<>();
 
    private LocalDateTime createdAt;
 
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
 
    public void addMember(User user) {
        if (this.members == null) {
            this.members = new ArrayList<>();
        }
        if (!this.members.contains(user)) {
            this.members.add(user);
        }
    }
}