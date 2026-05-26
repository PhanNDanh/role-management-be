package com.example.keycloakdemo.repository;

import com.example.keycloakdemo.entity.RealmConfig;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RealmConfigRepository extends JpaRepository<RealmConfig, Long> {

    Optional<RealmConfig> findByRealmAndActiveTrue(String realmName);
}