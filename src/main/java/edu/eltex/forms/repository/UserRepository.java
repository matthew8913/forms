package edu.eltex.forms.repository;

import edu.eltex.forms.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
    boolean existsByUsername(String username);
}
