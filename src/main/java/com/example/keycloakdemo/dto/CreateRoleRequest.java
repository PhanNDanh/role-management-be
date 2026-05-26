package com.example.keycloakdemo.dto;

import lombok.Data;

@Data
public class CreateRoleRequest {

    private String code;

    private String name;

    private String description;

    private Integer status;
}