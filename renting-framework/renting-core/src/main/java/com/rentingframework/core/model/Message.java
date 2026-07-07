package com.rentingframework.core.model;

import java.time.LocalDateTime;
 
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
 * A message inside a Group. Identical need across all three apps, so
 * this stays a plain entity — no app has needed to extend it so far. If
 * one ever does (e.g. attaching a receipt to a carpooling message),
 * switch this to JOINED inheritance the same way the other core
 * entities are set up.
 */
@Entity
@Table(name = "messages")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Message {
 
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
 
    @ManyToOne
    @JoinColumn(name = "group_id", nullable = false)
    private Group group;
 
    @ManyToOne
    @JoinColumn(name = "sender_id", nullable = false)
    private User sender;
 
    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;
 
    private LocalDateTime sentAt;
 
    @PrePersist
    protected void onCreate() {
        this.sentAt = LocalDateTime.now();
    }
}