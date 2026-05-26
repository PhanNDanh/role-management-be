package com.example.keycloakdemo.service;

import com.example.keycloakdemo.dto.RolePermissionTreeResponse;
import com.example.keycloakdemo.dto.UpdateRolePermissionRequest;
import com.example.keycloakdemo.entity.Function;
import com.example.keycloakdemo.entity.RolePermission;
import com.example.keycloakdemo.repository.FunctionRepository;
import com.example.keycloakdemo.repository.RolePermissionRepository;
import com.example.keycloakdemo.util.RealmUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RolePermissionService {

    private final FunctionRepository functionRepository;
    private final RolePermissionRepository rolePermissionRepository;

    public List<RolePermissionTreeResponse> getRolePermissionTree(Long roleId) {

        // 1. Lấy realm từ header x-kxh-realm
        String realm = RealmUtils.getRealmFromRequest();

        // 2. Lấy toàn bộ function thuộc realm hiện tại
        List<Function> functions =
                functionRepository.findByRealmOrderByRankAsc(realm);

        // 3. Lấy permission của role
        List<RolePermission> permissions =
                rolePermissionRepository.findByRoleId(roleId);

        // 4. Map functionId -> permission để tra nhanh
        Map<Long, RolePermission> permissionMap =
                permissions.stream()
                        .collect(Collectors.toMap(
                                RolePermission::getFunctionId,
                                item -> item
                        ));

        // 5. Map functionId -> function response
        Map<Long, RolePermissionTreeResponse> responseMap =
                new LinkedHashMap<>();

        for (Function function : functions) {
            RolePermission permission =
                    permissionMap.get(function.getId());

            RolePermissionTreeResponse response =
                    RolePermissionTreeResponse.builder()
                            .id(permission == null ? null : permission.getId())
                            .funcId(function.getId())
                            .name(function.getName())
                            .level(0)
                            .path(function.getPath())
                            .canView(permission == null ? null : permission.getCanView())
                            .canCreate(permission == null ? null : permission.getCanCreate())
                            .canEdit(permission == null ? null : permission.getCanEdit())
                            .canDelete(permission == null ? null : permission.getCanDelete())
                            .canImport(permission == null ? null : permission.getCanImport())
                            .canExport(permission == null ? null : permission.getCanExport())
                            .canApprove(permission == null ? null : permission.getCanApprove())
                            .children(new ArrayList<>())
                            .build();

            responseMap.put(function.getId(), response);
        }

        // 6. Build cây cha/con
        List<RolePermissionTreeResponse> roots = new ArrayList<>();

        for (Function function : functions) {
            RolePermissionTreeResponse current =
                    responseMap.get(function.getId());

            if (function.getParentId() == null) {
                current.setLevel(0);
                roots.add(current);
            } else {
                RolePermissionTreeResponse parent =
                        responseMap.get(function.getParentId());

                if (parent != null) {
                    current.setLevel(parent.getLevel() + 1);
                    parent.getChildren().add(current);
                }
            }
        }

        return roots;
    }

    @Transactional
    public void updateRolePermissions(
            Long roleId,
            UpdateRolePermissionRequest request
    ) {
        rolePermissionRepository.deleteByRoleId(roleId);

        List<RolePermission> permissions = request.getPermissions()
                .stream()
                .map(item -> RolePermission.builder()
                        .roleId(roleId)
                        .functionId(item.getFunctionId())
                        .canView(item.getCanView())
                        .canCreate(item.getCanCreate())
                        .canEdit(item.getCanEdit())
                        .canDelete(item.getCanDelete())
                        .canImport(item.getCanImport())
                        .canExport(item.getCanExport())
                        .canApprove(item.getCanApprove())
                        .build())
                .toList();

        rolePermissionRepository.saveAll(permissions);
    }
}