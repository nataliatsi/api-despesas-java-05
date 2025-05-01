package com.github.progirls.despesas.api.despesas_api.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.progirls.despesas.api.despesas_api.dto.UsuarioRegisterDto;
import com.github.progirls.despesas.api.despesas_api.entities.Usuario;
import com.github.progirls.despesas.api.despesas_api.repository.UsuarioRepository;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class UsuarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        usuarioRepository.deleteAll();

        Usuario usuario = new Usuario();
        usuario.setNome("Usuário Existente");
        usuario.setEmail("existente@teste.com");
        usuario.setSenha(passwordEncoder.encode("Senha123."));
        usuario.setDataCriacao(LocalDateTime.now());

        usuarioRepository.save(usuario);
    }

    @Test
    void deveCadastrarUsuarioComSucesso() throws Exception {
        var novoUsuario = new Usuario();
        novoUsuario.setNome("Usuário Novo");
        novoUsuario.setEmail("novo@teste.com");
        novoUsuario.setSenha("Senha1234.");
        novoUsuario.setDataCriacao(LocalDateTime.now());

        mockMvc.perform(post("/api/v1/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(novoUsuario)))
                .andExpect(status().isCreated())
                .andExpect(content().string("Usuário cadastrado com sucesso!"));
    }

    @Test
    void deveRetornar400ParaDadosInvalidos() throws Exception {
        var usuarioInvalido = new UsuarioRegisterDto(null, null, null);

        mockMvc.perform(post("/api/v1/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(usuarioInvalido)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deveRetornar409ParaEmailJaCadastrado() throws Exception {
        var usuarioExistente = new Usuario();
        usuarioExistente.setNome("Uusário Teste");
        usuarioExistente.setEmail("existente@teste.com");
        usuarioExistente.setSenha("Senha123.");
        usuarioExistente.setDataCriacao(LocalDateTime.now());

        mockMvc.perform(post("/api/v1/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(usuarioExistente)))
                .andExpect(status().isConflict())
                .andExpect(content().string("Email já cadastrado, por favor tente outro."));
    }
}
