package com.campushome.api.model;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Isso aqui faz o Postgres cuidar de qual é o ID de cada usuário novo
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    private String course; // Literalmente o curso do usuário

    @Column(columnDefinition = "TEXT")
    private String bio; // Para o perfil e matching.

    // Um usuário pode ter vários anúncios
    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL)
    private List<Advertisement> advertisements;

}
