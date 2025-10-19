package com.project.auth.config;

import com.project.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;

/**
 * @author Murat Saka
 * @created 19/10/2025 - 12:07
 * @project AuthServer
 */
@Configuration
@RequiredArgsConstructor
public class SercurityConfig {
    private final UserRepository userRepository;
}
