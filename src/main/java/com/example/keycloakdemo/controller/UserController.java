package com.example.keycloakdemo.controller;

import com.example.keycloakdemo.dto.CreateUserRequest;
import com.example.keycloakdemo.entity.User;
import com.example.keycloakdemo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public User createUser(
            @RequestBody CreateUserRequest request
    ) {

        return userService.createUser(request);
    }
}