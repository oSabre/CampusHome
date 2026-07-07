package com.rentingframework.core.repository;
 
import java.util.List;
 
import org.springframework.data.jpa.repository.JpaRepository;
 
import com.rentingframework.core.model.Task;
 
public interface TaskRepository extends JpaRepository<Task, Long> {
 
    List<Task> findByGroupId(Long groupId);
 
}