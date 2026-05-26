package com.example.keycloakdemo.service;

import com.example.keycloakdemo.constant.UserStatus;
import com.example.keycloakdemo.dto.CreateUserRequest;
import com.example.keycloakdemo.entity.Role;
import com.example.keycloakdemo.entity.User;
import com.example.keycloakdemo.entity.UserRoleMapping;
import com.example.keycloakdemo.repository.RoleRepository;
import com.example.keycloakdemo.repository.UserRepository;
import com.example.keycloakdemo.repository.UserRoleMappingRepository;
import com.example.keycloakdemo.util.RealmUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserRoleMappingRepository userRoleMappingRepository;
    private final KeycloakAdminService keycloakAdminService;

    @Transactional
    public User createUser(CreateUserRequest request) {
        String realm = RealmUtils.getRealmFromRequest();

        User savedUser = null;
        String keycloakUserId = null;

        try {
            savedUser = userRepository.save(
                    User.builder()
                            .username(request.getUsername())
                            .email(request.getEmail())
                            .fullName(request.getFullName())
                            .keycloakId(null)
                            .realm(realm)
                            .status(UserStatus.INACTIVE)
                            .build()
            );

            keycloakUserId = keycloakAdminService.createUser(realm, request);

            List<Role> roles = roleRepository.findByIdInAndRealm(
                    request.getRoleIds(),
                    realm
            );

            if (roles.size() != request.getRoleIds().size()) {
                throw new RuntimeException("Some roles not found in realm: " + realm);
            }

            final User finalSavedUser = savedUser;

            List<UserRoleMapping> mappings = roles.stream()
                    .map(role -> UserRoleMapping.builder()
                            .userId(finalSavedUser.getId())
                            .roleId(role.getId())
                            .build())
                    .collect(Collectors.toList());

            userRoleMappingRepository.saveAll(mappings);

            List<String> roleCodes = roles.stream()
                    .map(Role::getCode)
                    .collect(Collectors.toList());

            keycloakAdminService.assignRoles(realm, keycloakUserId, roleCodes);

            savedUser.setKeycloakId(keycloakUserId);
            savedUser.setStatus(UserStatus.ACTIVE);

            return userRepository.save(savedUser);

        } catch (Exception e) {
            if (keycloakUserId != null) {
                keycloakAdminService.deleteUser(realm, keycloakUserId);
            }

            if (savedUser != null && savedUser.getId() != null) {
                userRoleMappingRepository.deleteAll(
                        userRoleMappingRepository.findByUserId(savedUser.getId())
                );
                userRepository.delete(savedUser);
            }

            throw new RuntimeException("Create user failed: " + e.getMessage(), e);
        }
    }
}