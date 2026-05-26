package com.example.keycloakdemo.repository;

import com.example.keycloakdemo.entity.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Set;

public interface RoleRepository extends JpaRepository<Role, Long> {

    List<Role> findByRealm(String realmName);

    List<Role> findByIdInAndRealm(Set<Long> ids, String realmName);

    boolean existsByCodeAndRealm(String code, String realmName);

    @Query("""
        SELECT r
        FROM Role r
        WHERE r.realm = :realm
        AND (:status IS NULL OR r.status = :status)
        AND (
            :keyword IS NULL
            OR LOWER(r.code) LIKE LOWER(CONCAT('%', :keyword, '%'))
            OR LOWER(r.name) LIKE LOWER(CONCAT('%', :keyword, '%'))
        )
    """)
    Page<Role> searchRoles(
            String realm,
            String keyword,
            Integer status,
            Pageable pageable
    );
}