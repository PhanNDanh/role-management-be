package com.example.keycloakdemo.dto;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RolePermissionTreeResponse {

    private Long id;

    private Long funcId;

    private String name;

    private Integer level;

    private Integer canView;

    private Integer canCreate;

    private Integer canEdit;

    private Integer canDelete;

    private Integer canImport;

    private Integer canExport;

    private Integer canApprove;

    private String path;

    @Builder.Default
    private List<RolePermissionTreeResponse> children = new ArrayList<>();
}