package com.expert.api.model;

import com.expert.api.enums.TaskType;
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
 * Same reasoning as CarpoolingTask: most tasks here are auto-generated
 * when a Session completes (SESSION_NOTES, INVOICE), not manually created
 * like Housing's chores, and ExpertRewardPolicy will reward differently
 * per type - INVOICE probably matters more than a manual note, same
 * "gamification is fixed, what it rewards is variable" story as
 * Carpooling's REFUEL/MILEAGE_LOG split.
 */
@Entity
@Table(name = "expert_tasks")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ExpertTask extends Task {

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TaskType taskType;
}