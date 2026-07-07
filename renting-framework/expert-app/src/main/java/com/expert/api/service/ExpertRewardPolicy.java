package com.expert.api.service;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.expert.api.enums.TaskType;
import com.expert.api.model.ExpertTask;
import com.rentingframework.core.model.Task;
import com.rentingframework.core.model.User;
import com.rentingframework.core.service.RewardPolicy;
import com.rentingframework.core.service.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ExpertRewardPolicy implements RewardPolicy {

    private static final Map<TaskType, Integer> XP_BY_TYPE = Map.of(
            TaskType.SESSION_NOTES, 10,
            TaskType.INVOICE, 20,
            TaskType.MANUAL, 10
    );

    private final UserService userService;

    @Override
    public void onTaskToggled(User user, Task task, boolean nowCompleted) {
        TaskType type = ((ExpertTask) task).getTaskType();
        int xpValue = XP_BY_TYPE.getOrDefault(type, 10);

        int currentXp = user.getReputationScore() != null ? user.getReputationScore() : 0;
        int delta = nowCompleted ? xpValue : -Math.min(xpValue, currentXp);
        userService.addReputationScore(user.getId(), delta);
    }
}