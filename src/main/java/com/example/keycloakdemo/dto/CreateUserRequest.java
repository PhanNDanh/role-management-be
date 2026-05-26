package com.example.keycloakdemo.dto;

import lombok.Data;

import java.util.Set;

@Data
public class CreateUserRequest {

    private String username;

    private String email;

    private String fullName;

    private String password;

    // ID role trong DB app, ví dụ 1 = USER, 2 = ADMIN
    private Set<Long> roleIds;
}