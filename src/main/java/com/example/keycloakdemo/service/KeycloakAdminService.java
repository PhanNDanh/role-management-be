package com.example.keycloakdemo.service;

import com.example.keycloakdemo.config.KeycloakProperties;
import com.example.keycloakdemo.dto.CreateUserRequest;
import com.example.keycloakdemo.entity.RealmConfig;
import com.example.keycloakdemo.repository.RealmConfigRepository;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class KeycloakAdminService {

    private final KeycloakProperties props;
    private final RealmConfigRepository realmConfigRepository;

    private Keycloak getClient(RealmConfig config) {
        return KeycloakBuilder.builder()
                .serverUrl(config.getServerUrl())
                .realm(config.getRealm())
                .grantType(OAuth2Constants.CLIENT_CREDENTIALS)
                .clientId(config.getClientId())
                .clientSecret(config.getClientSecret())
                .build();
    }

    public String createUser(String realm, CreateUserRequest request) {

        RealmConfig config = realmConfigRepository
                .findByRealmAndActiveTrue(realm)
                .orElseThrow(() -> new RuntimeException("Realm config not found: " + realm));

        UserRepresentation user = new UserRepresentation();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        String fullName = request.getFullName().trim();

        String firstName = fullName;
        String lastName = "-";

        String[] parts = fullName.split("\\s+");

        if (parts.length > 1) {
            lastName = parts[parts.length - 1];

            firstName = String.join(
                    " ",
                    Arrays.copyOf(parts, parts.length - 1)
            );
        }
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEnabled(true);

        CredentialRepresentation credential = new CredentialRepresentation();
        credential.setType(CredentialRepresentation.PASSWORD);
        credential.setValue(request.getPassword());
        credential.setTemporary(false);

        user.setCredentials(List.of(credential));

        try (Keycloak keycloak = getClient(config)) {
            Response response = keycloak
                    .realm(config.getRealm())
                    .users()
                    .create(user);

            if (response.getStatus() != 201) {
                throw new RuntimeException(
                        "Keycloak create user failed, status = " + response.getStatus()
                );
            }

            String location = response.getLocation().toString();
            return location.substring(location.lastIndexOf("/") + 1);
        }
    }

    public void assignRoles(
            String realmName,
            String keycloakUserId,
            List<String> roleNames
    ) {

        RealmConfig config =
                realmConfigRepository
                        .findByRealmAndActiveTrue(realmName)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Realm config not found: " + realmName
                                ));

        try (Keycloak keycloak = getClient(config)) {

            RealmResource realm =
                    keycloak.realm(config.getRealm());

            List<RoleRepresentation> roles =
                    roleNames.stream()
                            .map(roleName ->
                                    realm.roles()
                                            .get(roleName)
                                            .toRepresentation()
                            )
                            .toList();

            realm.users()
                    .get(keycloakUserId)
                    .roles()
                    .realmLevel()
                    .add(roles);
        }
    }

    public void deleteUser(
            String realmName,
            String keycloakUserId
    ) {

        RealmConfig config =
                realmConfigRepository
                        .findByRealmAndActiveTrue(realmName)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Realm config not found: " + realmName
                                ));

        try (Keycloak keycloak = getClient(config)) {

            keycloak.realm(config.getRealm())
                    .users()
                    .get(keycloakUserId)
                    .remove();
        }
    }
}