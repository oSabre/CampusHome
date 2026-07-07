package com.expert.api.model;

import com.rentingframework.core.model.Group;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Same Group/Message infrastructure as the other two apps, but this one
 * is always exactly 2 members (client + specialist) - no ambiguity about
 * "how many" like Housing's N residents or Carpooling's variable pool
 * size. engagementNotes is this app's version of Housing's rules /
 * Carpooling's usagePolicy - same architectural slot, third different
 * domain meaning (ongoing shared context about the engagement, e.g.
 * "focusing on resume review this month").
 */
@Entity
@Table(name = "expert_groups")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ExpertGroup extends Group {

    @Column(columnDefinition = "TEXT")
    private String engagementNotes;
}