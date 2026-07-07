package com.rentingframework.core.service;
 
import com.rentingframework.core.exception.ResourceNotFoundException;
import com.rentingframework.core.model.User;
import com.rentingframework.core.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
 
@Service
@RequiredArgsConstructor
public class UserService {
 
    private final UserRepository userRepository;
 
    /**
     * Salva qualquer tipo de usuário (independente da subclasse).
     * Garante a regra universal de e-mail único do framework.
     */
    @Transactional
    public User registerUser(User user) {
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new RuntimeException("Este e-mail já está em uso na plataforma.");
        }
        // Como o JPA entende polimorfismo, se você passar um CampusUser aqui,
        // ele salva na tabela 'users' e na tabela 'campushome_users' automaticamente.
        return userRepository.save(user);
    }
 
    /**
     * Fluxo de login estrito e genérico.
     * Retorna a entidade User, permitindo que a aplicação converta para o DTO específico dela.
     */
    @Transactional(readOnly = true)
    public User login(String email, String password) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("E-mail não cadastrado."));
 
        if (!user.getPassword().equals(password)) {
            throw new RuntimeException("Senha incorreta.");
        }
 
        return user;
    }
 
    @Transactional(readOnly = true)
    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado."));
    }
    
    /**
     * Método auxiliar genérico para o motor de gamificação.
     */
    @Transactional
    public void addReputationScore(Long userId, int points) {
        User user = findById(userId);
        user.setReputationScore(user.getReputationScore() + points);
        userRepository.save(user);
    }
}