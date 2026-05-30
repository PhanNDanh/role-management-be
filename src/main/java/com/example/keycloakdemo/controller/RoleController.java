package com.example.keycloakdemo.controller;

import com.example.keycloakdemo.dto.CreateRoleRequest;
import com.example.keycloakdemo.dto.RoleListResponse;
import com.example.keycloakdemo.dto.RoleResponse;
import com.example.keycloakdemo.dto.UpdateRoleRequest;
import com.example.keycloakdemo.entity.Role;
import com.example.keycloakdemo.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/roles")
@RequiredArgsConstructor
public class RoleController {

    private final RoleService roleService;

    @GetMapping
    public List<Role> getRoles() {
        return roleService.getRoles();
    }

    @GetMapping("/{id}")
    public Role getRole(@PathVariable Long id) {
        return roleService.getRole(id);
    }

    @PostMapping
    public Role createRole(@RequestBody CreateRoleRequest request) {
        return roleService.createRole(request);
    }

    @PutMapping("/{id}")
    public Role updateRole(
            @PathVariable Long id,
            @RequestBody UpdateRoleRequest request
    ) {
        return roleService.updateRole(id, request);
    }

    @DeleteMapping("/{id}")
    public void deleteRole(@PathVariable Long id) {
        roleService.deleteRole(id);
    }

    @GetMapping("/search")
    public Page<RoleListResponse> getRoles(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Integer status,
            Pageable pageable
    ) {
        return roleService.getRoles(keyword, status, pageable);
    }

//    @GetMapping("/elastic-/search")
//    public List<RoleResponse> searchRoles(
//            @RequestParam String keyword
//    ) {
//        return roleService.searchRoles(keyword);
//    }
//
//    @PostMapping("/sync-elastic")
//    public String syncRolesToElastic() {
//        roleService.syncAllRolesToElastic();
//        return "OK";
//    }
}