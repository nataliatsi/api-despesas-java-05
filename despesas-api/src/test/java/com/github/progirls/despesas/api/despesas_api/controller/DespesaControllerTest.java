package com.github.progirls.despesas.api.despesas_api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.progirls.despesas.api.despesas_api.dto.NovaDespesaDTO;
import com.github.progirls.despesas.api.despesas_api.entities.Usuario;
import com.github.progirls.despesas.api.despesas_api.repository.DespesaRepository;
import com.github.progirls.despesas.api.despesas_api.repository.UsuarioRepository;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// Em caso de emergência utilizar anotação na classe:
// @DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class DespesaControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    private String userEmail;
    private final String userSenha = "Teste@123";
    private String token;
    @Autowired
    private DespesaRepository despesaRepository;

    @BeforeEach
    void setUp() throws Exception {
        despesaRepository.deleteAll();
        usuarioRepository.deleteAll();

        userEmail = "teste" + System.currentTimeMillis() + "@teste.com";

        Usuario user = new Usuario(
                null,
                "Teste",
                userEmail,
                passwordEncoder.encode(userSenha),
                LocalDateTime.now());
        usuarioRepository.save(user);

        MvcResult loginResult = mockMvc.perform(post("/api/v1/login")
                        .with(httpBasic(userEmail, userSenha)))
                .andExpect(status().isOk())
                .andReturn();

        String responseJson = loginResult.getResponse().getContentAsString();
        token = new ObjectMapper().readTree(responseJson).get("token").asText();

    }

    @AfterEach
    void tearDown() {
        despesaRepository.deleteAll();
        usuarioRepository.deleteAll();
    }

    @Test
    void deveCriarDespesaComSucesso() throws Exception {
        var response = mockMvc.perform(post("/api/v1/login")
                        .with(httpBasic(userEmail, userSenha)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists())
                .andReturn();

        String json = response.getResponse().getContentAsString();
        String token = JsonPath.read(json, "$.token");

        NovaDespesaDTO dto = new NovaDespesaDTO(
                "Shopping",
                100.0,
                "Shopping",
                2,
                LocalDate.now(),
                null,
                false
        );

        mockMvc.perform(post("/api/v1/despesas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto))
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isCreated());
    }

    @Test
    void deveRetornarErro400() throws Exception{
        String despesaInvalida = """
                {
                    "valor" = 230.0,
                    "descricao" = "Despesa Inválida"
                }
                """;

        mockMvc.perform(post("/api/v1/despesas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(despesaInvalida)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deveRetornarErro401() throws Exception{
        NovaDespesaDTO dto = new NovaDespesaDTO(
                "Shopping",
                100.0,
                "Shopping",
                2,
                LocalDate.now(),
                null,
                false
        );

        mockMvc.perform(post("/api/v1/despesas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto))
                        .header("Authorization", "Bearer TOKEN_INVALIDO"))
                .andExpect(status().isUnauthorized());
    }
}
