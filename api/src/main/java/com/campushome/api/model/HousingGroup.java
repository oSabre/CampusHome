package com.campushome.api.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "housing_groups")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HousingGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String rules;

    @OneToOne
    @JoinColumn(name = "advertisement_id", referencedColumnName = "id")
    private Advertisement advertisement;

    @ManyToMany
    @JoinTable(
        name = "housing_group_residents",
        joinColumns = @JoinColumn(name = "housing_group_id"),
        inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private List<User> residents = new ArrayList<>();

    public void addResident(User student) {
        if (this.residents == null) {
            this.residents = new ArrayList<>();
        }
        if (!this.residents.contains(student)) {
            this.residents.add(student);
        }
    }

}
