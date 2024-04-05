package org.example.qthotelbe.repository;

import org.example.qthotelbe.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository  extends JpaRepository<Role,Long> {
    Optional<Role> findRoleByName(String roleUser);

    boolean existsByName(String roleName);
}
