package com.github.progirls.despesas.api.despesas_api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/api/v1/protected")
@SecurityRequirement(name = "bearer-key")
public class TestController {

    @GetMapping("/hello")
    public ResponseEntity<String> protectedEndpoint() {
        return ResponseEntity.ok("Acesso autorizado! Você está autenticado com JWT.");
    }

}
