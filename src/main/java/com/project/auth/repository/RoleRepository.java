package com.project.auth.repository;

import com.project.auth.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Murat Saka
 * @created 18/10/2025 - 13:02
 * @project AuthServer
 */
@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
}
