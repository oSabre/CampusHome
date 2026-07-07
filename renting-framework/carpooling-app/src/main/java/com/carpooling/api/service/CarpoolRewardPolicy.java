package com.carpooling.api.service;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.carpooling.api.enums.TaskType;
import com.carpooling.api.model.CarpoolingTask;
import com.rentingframework.core.model.Task;
import com.rentingframework.core.model.User;
import com.rentingframework.core.service.RewardPolicy;
import com.rentingframework.core.service.UserService;

import lombok.RequiredArgsConstructor;

/**
 * Same mandatory gamification hook as HousingRewardPolicy, but the reward
 * amount now varies by TaskType instead of one flat number - a more
 * concrete illustration of "gamification is fixed, what it rewards is
 * variable" than Housing's version, since Housing's Task has no type to
 * vary by at all.
 */
@Service
@RequiredArgsConstructor
public class CarpoolRewardPolicy implements RewardPolicy {

    private static final Map<TaskType, Integer> XP_BY_TYPE = Map.of(
            TaskType.REFUEL, 15,
            TaskType.MILEAGE_LOG, 5,
            TaskType.MAINTENANCE_SPLIT, 20,
            TaskType.MANUAL, 10
    );

    private final UserService userService;

    @Override
    public void onTaskToggled(User user, Task task, boolean nowCompleted) {
        // Safe here for the same reason as the CarUser cast elsewhere:
        // every Task row in this app is actually a CarpoolingTask.
        TaskType type = ((CarpoolingTask) task).getTaskType();
        int xpValue = XP_BY_TYPE.getOrDefault(type, 10);

        int currentXp = user.getReputationScore() != null ? user.getReputationScore() : 0;
        int delta = nowCompleted ? xpValue : -Math.min(xpValue, currentXp);
        userService.addReputationScore(user.getId(), delta);
    }
}