package edu.eltex.forms.repository;

import edu.eltex.forms.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    boolean existsByUsername(String username);

    Optional<User> findByUsername(String username);

    Optional<User> findByRefreshToken(String refreshToken);
}
