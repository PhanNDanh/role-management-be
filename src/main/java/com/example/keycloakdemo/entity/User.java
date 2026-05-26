package com.example.keycloakdemo.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "users")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    private String email;

    private String fullName;

    private String keycloakId;

    /**
     * 0 = DRAFT
     * 1 = ACTIVE
     * 2 = INACTIVE
     * 3 = LOCKED
     */
    private Integer status;

    private String realm;
}