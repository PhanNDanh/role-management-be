package com.example.keycloakdemo.dto;

import lombok.Data;

import java.util.List;

@Data
public class UpdateRolePermissionRequest {

    private List<Item> permissions;

    @Data
    public static class Item {

        private Long functionId;

        private Integer canView;

        private Integer canCreate;

        private Integer canEdit;

        private Integer canDelete;

        private Integer canImport;

        private Integer canExport;

        private Integer canApprove;
    }
}