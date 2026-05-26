package com.example.keycloakdemo.repository;

import com.example.keycloakdemo.entity.UserRoleMapping;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRoleMappingRepository
        extends JpaRepository<UserRoleMapping, Long> {

    List<UserRoleMapping> findByUserId(Long userId);
}