package com.github.progirls.despesas.api.despesas_api.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.progirls.despesas.api.despesas_api.security.AuthenticationService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;

@RestController
@RequestMapping("/api/v1/login")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping
    @SecurityRequirement(name = "basicAuth")
    public ResponseEntity<?> authenticate(Authentication authentication) {
        String token = authenticationService.autheticate(authentication);
        return ResponseEntity.ok(Map.of("token", token));
    }

}
