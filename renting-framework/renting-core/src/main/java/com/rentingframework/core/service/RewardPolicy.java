package com.rentingframework.core.service;
 
import com.rentingframework.core.model.Task;
import com.rentingframework.core.model.User;
 
/**
 * Fixed Hot Spot: Contrato obrigatório de gamificação.
 * Toda aplicação deve prover um Bean implementando esta interface.
 */
public interface RewardPolicy {
    
    /**
     * Invocado automaticamente pelo framework sempre que uma Task muda de status.
     * * @param user O usuário que receberá (ou perderá) o ganho de reputação.
     * @param task A tarefa que foi alterada (útil para ler instâncias/subclasses específicas).
     * @param nowCompleted O novo status da tarefa (true = recém concluída, false = reaberta/desmarcada).
     */
    void onTaskToggled(User user, Task task, boolean nowCompleted);
}