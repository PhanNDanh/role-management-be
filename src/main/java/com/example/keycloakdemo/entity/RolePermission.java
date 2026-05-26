package com.example.keycloakdemo.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "role_permission")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RolePermission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long roleId;

    private Long functionId;

    private Integer canView;

    private Integer canCreate;

    private Integer canEdit;

    private Integer canDelete;

    private Integer canImport;

    private Integer canExport;

    private Integer canApprove;
}