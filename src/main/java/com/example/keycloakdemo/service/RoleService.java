package com.example.keycloakdemo.service;

import com.example.keycloakdemo.elastic.document.RoleDocument;
import com.example.keycloakdemo.dto.CreateRoleRequest;
import com.example.keycloakdemo.dto.RoleListResponse;
import com.example.keycloakdemo.dto.RoleResponse;
import com.example.keycloakdemo.dto.UpdateRoleRequest;
import com.example.keycloakdemo.entity.Role;
import com.example.keycloakdemo.repository.RoleRepository;
import com.example.keycloakdemo.repository.RoleSearchRepository;
import com.example.keycloakdemo.util.RealmUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleService {

    private final RoleRepository roleRepository;
    private final RoleSearchRepository roleSearchRepository;
    private final ElasticsearchOperations elasticsearchOperations;

    public List<Role> getRoles() {
        String realm = RealmUtils.getRealmFromRequest();
        return roleRepository.findByRealm(realm);
    }

    public Role getRole(Long id) {
        return roleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Role not found"));
    }

    public Role createRole(CreateRoleRequest request) {
        String realm = RealmUtils.getRealmFromRequest();

        if (roleRepository.existsByCodeAndRealm(request.getCode(), realm)) {
            throw new RuntimeException("Role code already exists in realm: " + realm);
        }

        Role role = Role.builder()
                .code(request.getCode())
                .name(request.getName())
                .description(request.getDescription())
                .status(request.getStatus() == null ? 1 : request.getStatus())
                .realm(realm)
                .build();


        Role roleSave = roleRepository.save(role);

        syncRoleToElastic(roleSave);

        return roleSave;
    }

    public Role updateRole(Long id, UpdateRoleRequest request) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Role not found"));

        role.setCode(request.getCode());
        role.setName(request.getName());
        role.setDescription(request.getDescription());
        role.setStatus(request.getStatus());

        return roleRepository.save(role);
    }

    public void deleteRole(Long id) {
        roleRepository.deleteById(id);
    }

    public Page<RoleListResponse> getRoles(
            String keyword,
            Integer status,
            Pageable pageable
    ) {
        String realm = RealmUtils.getRealmFromRequest();

        String searchKeyword =
                keyword == null || keyword.isBlank()
                        ? null
                        : keyword.trim();

        return roleRepository
                .searchRoles(realm, searchKeyword, status, pageable)
                .map(role -> RoleListResponse.builder()
                        .id(role.getId())
                        .code(role.getCode())
                        .name(role.getName())
                        .description(role.getDescription())
                        .status(role.getStatus())
                        .build());
    }

    public List<RoleResponse> searchRoles(String keyword) {
        String realm = RealmUtils.getRealmFromRequest();

        NativeQuery query = NativeQuery.builder()
                .withQuery(q -> q.bool(b -> b
                        .must(m -> m.term(t -> t
                                .field("realm.keyword")
                                .value(realm)
                        ))
                        .must(m -> m.bool(bb -> bb
                                .should(s -> s.wildcard(w -> w
                                        .field("code.keyword")
                                        .value("*" + keyword + "*")
                                        .caseInsensitive(true)
                                ))
                                .should(s -> s.wildcard(w -> w
                                        .field("name.keyword")
                                        .value("*" + keyword + "*")
                                        .caseInsensitive(true)
                                ))
                                .should(s -> s.wildcard(w -> w
                                        .field("description.keyword")
                                        .value("*" + keyword + "*")
                                        .caseInsensitive(true)
                                ))
                                .minimumShouldMatch("1")
                        ))
                ))
                .build();

        SearchHits<RoleDocument> hits =
                elasticsearchOperations.search(query, RoleDocument.class);

        return hits.stream()
                .map(SearchHit::getContent)
                .map(this::mapRoleDocumentToResponse)
                .toList();
    }

    private void syncRoleToElastic(Role role) {
        RoleDocument doc = RoleDocument.builder()
                .id(String.valueOf(role.getId()))
                .code(role.getCode())
                .name(role.getName())
                .description(role.getDescription())
                .status(role.getStatus())
                .realm(role.getRealm())
                .build();

        roleSearchRepository.save(doc);
    }

    private RoleResponse mapRoleDocumentToResponse(RoleDocument doc) {
        return RoleResponse.builder()
                .id(Long.valueOf(doc.getId()))
                .code(doc.getCode())
                .name(doc.getName())
                .description(doc.getDescription())
                .status(doc.getStatus())
                .realm(doc.getRealm())
                .build();
    }

    public void syncAllRolesToElastic() {

        List<Role> roles = roleRepository.findAll();

        List<RoleDocument> documents = roles.stream()
                .map(role -> RoleDocument.builder()
                        .id(String.valueOf(role.getId()))
                        .code(role.getCode())
                        .name(role.getName())
                        .description(role.getDescription())
                        .realm(role.getRealm())
                        .status(role.getStatus())
                        .build())
                .toList();

        roleSearchRepository.saveAll(documents);
    }
}