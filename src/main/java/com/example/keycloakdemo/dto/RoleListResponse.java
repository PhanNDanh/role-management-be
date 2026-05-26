package com.example.keycloakdemo.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class RoleListResponse {

    private Long id;

    private String code;

    private String name;

    private String description;

    private Integer status;
}