package com.example.keycloakdemo.controller;

import com.example.keycloakdemo.dto.RolePermissionTreeResponse;
import com.example.keycloakdemo.dto.UpdateRolePermissionRequest;
import com.example.keycloakdemo.service.RolePermissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/roles-permissions")
@RequiredArgsConstructor
public class RolePermissionController {

    private final RolePermissionService rolePermissionService;

    @GetMapping("/{roleId}")
    public List<RolePermissionTreeResponse> getRolePermissions(
            @PathVariable Long roleId
    ) {
        return rolePermissionService.getRolePermissionTree(roleId);
    }

    @PutMapping("/{roleId}")
    public void updateRolePermissions(
            @PathVariable Long roleId,
            @RequestBody UpdateRolePermissionRequest request
    ) {
        rolePermissionService.updateRolePermissions(roleId, request);
    }
}