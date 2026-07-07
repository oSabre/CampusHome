package com.rentingframework.core.repository;
 
import java.util.List;
 
import org.springframework.data.jpa.repository.JpaRepository;
 
import com.rentingframework.core.model.Message;
 
public interface MessageRepository extends JpaRepository<Message, Long> {
 
    List<Message> findByGroupIdOrderBySentAtAsc(Long groupId);
 
}