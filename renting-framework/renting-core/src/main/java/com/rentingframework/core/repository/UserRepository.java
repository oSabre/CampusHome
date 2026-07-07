package com.rentingframework.core.repository;
 
import java.util.Optional;
 
import org.springframework.data.jpa.repository.JpaRepository;
 
import com.rentingframework.core.model.User;
 
public interface UserRepository extends JpaRepository<User, Long> {
 
    Optional<User> findByEmail(String email);
 
}