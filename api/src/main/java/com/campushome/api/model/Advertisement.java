package com.campushome.api.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

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

@Entity
@Table(name = "advertisements")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Advertisement {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String description;

    @Column(nullable = false)
    private BigDecimal price; // Usar BigDecimal para valores monetários

    private String neighborhood; // Bairro 
    
    private String address;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AdvertisementStatus status;

    private LocalDateTime createdAt;

    // Relacionamento: Muitos anúncios pertencem a um único usuário (Dono)
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User owner;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        if(this.status == null){
            this.status = AdvertisementStatus.AVAILABLE;
        }
    }
}
