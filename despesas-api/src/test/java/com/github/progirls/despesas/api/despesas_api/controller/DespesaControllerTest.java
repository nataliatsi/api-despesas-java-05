package com.github.progirls.despesas.api.despesas_api.controller;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.checkerframework.checker.units.qual.s;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;

import com.jayway.jsonpath.JsonPath;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.progirls.despesas.api.despesas_api.dto.AtualizarDespesaDTO;
import com.github.progirls.despesas.api.despesas_api.dto.NovaDespesaDTO;
import com.github.progirls.despesas.api.despesas_api.entities.Despesa;
import com.github.progirls.despesas.api.despesas_api.entities.Usuario;
import com.github.progirls.despesas.api.despesas_api.repository.DespesaRepository;
import com.github.progirls.despesas.api.despesas_api.repository.UsuarioRepository;

// Em caso de emergência utilizar anotação na classe:
// @DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class DespesaControllerTest {
    
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private DespesaRepository despesaRepository;

    private String userEmail;
    private final String userSenha = "Teste@123";
    private String token;

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
    @DisplayName("Deve criar uma despesa com sucesso retornando status 201")
    void deveCriarUmaDespesaComSucessoRetornar201() throws Exception {

        NovaDespesaDTO novaDespesa = new NovaDespesaDTO("MERCADO",
            250.5,
            "mercado compras",
            2,
            LocalDate.now(),
            null,
            false);

        mockMvc.perform(post("/api/v1/despesas")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(novaDespesa)))
                .andExpect(status().isCreated());

    }

    @Test
    @DisplayName("Deve retornar erro 400 por criar uma despesa com campos inválidos")
    void deveRetornarErro400DespesaComCampoInválidos() throws Exception {
                
        String despesaInvalida = """
                {
                    "valor": 235.0,
                    "descricao": "roupas",
                    "parcelamento": 3
                }
                """; 

        mockMvc.perform(post("/api/v1/despesas")
                .header("Authorization", "Bearer " + this.token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(despesaInvalida))
                .andExpect(status().isBadRequest());
    }
    
    @Test
    @DisplayName("Deve retornar erro 401 ao tentar criar uma despesa com token inválido")
    void deveRetornar401CriarDespesaComTokenInvalido() throws Exception {

        NovaDespesaDTO novaDespesa = new NovaDespesaDTO("SHOPPING",
                170.2,
                "shopping roupas",
                2,
                LocalDate.now(),
                null,
                false);

        mockMvc.perform(post("/api/v1/despesas")
                .header("Authorization", "Bearer tokenInvalido123")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(novaDespesa)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Deve listar todas as despesas sem filtro, porém páginadas")
    void deveListarTodasSemFiltrosComSucesso200() throws Exception {
        mockMvc.perform(get("/api/v1/despesas")
                .header("Authorization", "Bearer " + this.token))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Deve retornar as despesas com o filtro e status 200")
    void deveListarAsDespesasFiltradasComSucesso200() throws Exception {
        mockMvc.perform(get("/api/v1/despesas")
                .param("categoria", "TRANSPORTE")
                .header("Authorization", "Bearer " + this.token))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Deve retornar erro 400 ao tentar filtar a despesa")
    void deveRetornar400AoListarDespesaComFiltroErrado() throws Exception {
        mockMvc.perform(get("/api/v1/despesas")
                .param("categoria", "MERCADO")
                .param("dataInicio", "data invalida")
                .header("Authorization", "Bearer " + this.token))
                .andExpect(status().isBadRequest());
                
    }

    @Test
    @DisplayName("Deve retornar o erro 401 ao tentar listar as despesas com um token inválido")
    void deveRetornar401AoListarDespesaComUmTokenInvalido() throws Exception {
        mockMvc.perform(get("/api/v1/despesas")
                .header("Authorization", "Bearer token.invalido"))
                .andExpect(status().isUnauthorized());
    }

}
