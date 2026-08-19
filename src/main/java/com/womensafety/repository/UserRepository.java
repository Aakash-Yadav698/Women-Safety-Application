package com.womensafety.repository;

import com.womensafety.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    // Spring parses this method name and writes:
    // SELECT * FROM users WHERE email = ?
    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);
}
