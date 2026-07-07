package com.carpooling.api.model;

import com.carpooling.api.enums.TaskType;
import com.rentingframework.core.model.Task;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Unlike HousingTask (no extra fields, reused core's Task directly),
 * this one needs a real type — most Carpooling tasks are auto-generated
 * when a Booking completes (refuel, mileage log), not manually created
 * like Housing's chores, and CarpoolRewardPolicy will reward differently
 * depending on which type this is.
 */
@Entity
@Table(name = "carpooling_tasks")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CarpoolingTask extends Task {

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TaskType taskType;
}