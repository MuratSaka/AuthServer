package com.project.auth.repository;

import com.project.auth.entity.Token;
import com.project.auth.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

/**
 * @author Murat Saka
 * @created 19/10/2025 - 12:03
 * @project AuthServer
 */
public interface TokenRepository extends JpaRepository<Token, UUID> {
    List<Token> findByUser(User user);
    Token findByToken(String token);
    Token findByRefreshToken(String token);
}
