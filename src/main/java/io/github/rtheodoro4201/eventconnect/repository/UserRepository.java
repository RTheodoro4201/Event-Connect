package io.github.rtheodoro4201.eventconnect.repository;

import io.github.rtheodoro4201.eventconnect.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email); // Busca um usuário pelo e-mail

    boolean existsByEmail(String email); // Verifica se um e-mail já existe
}
