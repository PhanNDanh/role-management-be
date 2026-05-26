package com.example.keycloakdemo.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "realm_config")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RealmConfig {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String realm;

    private String clientId;

    private String clientSecret;

    private String serverUrl;

    private Boolean active;
}