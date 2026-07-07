package com.campushome.api.service;

import org.springframework.stereotype.Service;

import com.rentingframework.core.model.Task;
import com.rentingframework.core.model.User;
import com.rentingframework.core.service.RewardPolicy;
import com.rentingframework.core.service.UserService;

import lombok.RequiredArgsConstructor;

/**
 * Flat XP, same numbers as the original CampusHome innovation: +10 on
 * completing a task, -10 on un-completing it, never below 0. Core's
 * addReputationScore() doesn't clamp on its own, so the clamping happens
 * here by computing a safe delta before calling it.
 */
@Service
@RequiredArgsConstructor
public class HousingRewardPolicy implements RewardPolicy {

    private static final int XP_VALUE = 10;

    private final UserService userService;

    @Override
    public void onTaskToggled(User user, Task task, boolean nowCompleted) {
        int currentXp = user.getReputationScore() != null ? user.getReputationScore() : 0;
        int delta = nowCompleted ? XP_VALUE : -Math.min(XP_VALUE, currentXp);
        userService.addReputationScore(user.getId(), delta);
    }
}