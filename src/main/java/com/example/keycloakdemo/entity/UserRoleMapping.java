package com.example.keycloakdemo.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "user_role_mapping")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRoleMapping {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * user bên app DB
     */
    private Long userId;

    /**
     * role bên DB
     */
    private Long roleId;
}