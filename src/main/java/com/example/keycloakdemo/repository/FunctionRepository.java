package com.example.keycloakdemo.repository;

import com.example.keycloakdemo.entity.Function;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FunctionRepository extends JpaRepository<Function, Long> {

    List<Function> findByRealmOrderByRankAsc(String realm);
}