package com.github.progirls.despesas.api.despesas_api.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.github.progirls.despesas.api.despesas_api.entities.Usuario;
import com.github.progirls.despesas.api.despesas_api.repository.UsuarioRepository;
import com.jayway.jsonpath.JsonPath;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class AuthenticationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        usuarioRepository.deleteAll();

        Usuario user = new Usuario(
                null,
                "Teste",
                "natalia@teste.com",
                passwordEncoder.encode("12345"),
                LocalDateTime.now());

        usuarioRepository.save(user);
    }

    @Test
    void deveRetornarTokenComCredenciaisValidas() throws Exception {

        var response = mockMvc.perform(post("/api/v1/login")
                .with(httpBasic("natalia@teste.com", "12345")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists())
                .andReturn();

        String json = response.getResponse().getContentAsString();
        String token = JsonPath.read(json, "$.token");

        mockMvc.perform(get("/api/v1/protected/hello")
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
    }

    @Test
    void deveRetornar401ComCredenciaisInvalidas() throws Exception {
        mockMvc.perform(post("/api/v1/login")
                .with(httpBasic("nat@test.com", "senhaErrada")))
                .andExpect(status().isUnauthorized());
    }
}
