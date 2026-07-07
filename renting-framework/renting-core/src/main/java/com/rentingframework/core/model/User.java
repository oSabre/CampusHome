package com.rentingframework.core.model;
 
import java.time.LocalDateTime;
 
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
 
/**
 * Base user shared by every app. JOINED inheritance (not @MappedSuperclass)
 * because core services (RequestService, GroupService, MatchingStrategy)
 * need to hold real relationships to User — a @MappedSuperclass isn't an
 * entity, so it can't be the target of a @ManyToOne/@ManyToMany.
 *
 * Each app extends this with its own concrete User, adding whatever
 * domain-specific fields and role enum it needs:
 *
 *   @Entity
 *   @Table(name = "campushome_users")
 *   public class CampusUser extends User {
 *       private String course;
 *       @Enumerated(EnumType.STRING) private UserRole role; // STUDENT, OWNER
 *   }
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {
 
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
 
    @Column(nullable = false)
    private String name;
 
    @Column(unique = true, nullable = false)
    private String email;
 
    @Column(nullable = false)
    private String password;
 
    private String phone;
 
    @Column(columnDefinition = "TEXT")
    private String bio;
 
    /**
     * Generic gamification counter. This is the fixed hook every app must
     * wire a RewardPolicy into — what it represents (XP, a reliability
     * score, a rating) is a variable point, but the field itself, and the
     * guarantee that something updates it, is not optional.
     */
    private Integer reputationScore = 0;
 
    private LocalDateTime createdAt;
 
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}