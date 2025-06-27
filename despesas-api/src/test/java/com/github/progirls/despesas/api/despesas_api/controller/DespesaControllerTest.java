package com.github.progirls.despesas.api.despesas_api.controller;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.github.progirls.despesas.api.despesas_api.dto.AtualizarDespesaDTO;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.databind.ObjectMapper;
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

    private Usuario usuario;
    private String token;
    private static final String BASE_URL = "/api/v1/despesas";

    @BeforeEach
    void setUp() throws Exception {
        despesaRepository.deleteAll();
        usuarioRepository.deleteAll();

        String userEmail = "teste" + System.currentTimeMillis() + "@teste.com";

        String userSenha = "Teste@123";
        usuario = usuarioRepository.save(new Usuario(
                null,
                "Teste",
                userEmail,
                passwordEncoder.encode(userSenha),
                LocalDateTime.now()));
        usuarioRepository.save(usuario);

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
            false,
                true);

        mockMvc.perform(post(BASE_URL)
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(novaDespesa)))
                .andExpect(status().isCreated());

    }

    @Test
    @DisplayName("Deve retornar erro 400 por criar uma despesa com campos inválidos")
    void deveRetornarErro400DespesaComCampoInvalidos() throws Exception {
                
        String despesaInvalida = """
                {
                    "valor": 235.0,
                    "descricao": "roupas",
                    "parcelamento": 3
                }
                """; 

        mockMvc.perform(post(BASE_URL)
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
                false,
                true);

        mockMvc.perform(post(BASE_URL)
                .header("Authorization", "Bearer tokenInvalido123")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(novaDespesa)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Deve retornar 200 ao atualizar uma despesa ativa com sucesso")
    void deveRetornar200AoAtualizarDespesaComSucesso() throws Exception {
        Despesa despesa = new Despesa(
                null, usuario, "SAÚDE", 200.0, "remédio", 1,
                LocalDate.of(2025, 6, 20), null, false, true
        );
        despesa = despesaRepository.save(despesa);

        AtualizarDespesaDTO dto = new AtualizarDespesaDTO(
                "SAÚDE",
                350.0,
                "remédio atualizado",
                3,
                LocalDate.of(2025, 6, 25)
        );

        mockMvc.perform(put(BASE_URL + "/" + despesa.getId())
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.categoria").value("SAÚDE"))
                .andExpect(jsonPath("$.valor").value(350.0))
                .andExpect(jsonPath("$.descricao").value("remédio atualizado"))
                .andExpect(jsonPath("$.parcelamento").value(3));
    }

    @Test
    @DisplayName("Deve retornar 400 ao tentar atualizar uma despesa inativa")
    void deveRetornar400AoAtualizarDespesaInativa() throws Exception {
        Despesa despesaInativa = new Despesa(null, usuario, "SAÚDE", 200.0, "remédio", 1,
                LocalDate.of(2025, 6, 20), null, false, false);
        despesaInativa = despesaRepository.save(despesaInativa);

        AtualizarDespesaDTO dto = new AtualizarDespesaDTO(
                "Farmácia",
                300.0,
                "remédio atualizado",
                2,
                LocalDate.of(2025, 6, 25)
        );

        mockMvc.perform(put(BASE_URL + "/" + despesaInativa.getId())
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(Matchers.containsString("Não é possível editar uma despesa inativa")));
    }

    @Test
    @DisplayName("Deve retornar 403 ao tentar atualizar uma despesa de outro usuário")
    void deveRetornar403AoAtualizarDespesaDeOutroUsuario() throws Exception {
        Usuario outroUsuario = new Usuario(null, "Outro", "outro@email.com", passwordEncoder.encode("Senha@123"), LocalDateTime.now());
        usuarioRepository.save(outroUsuario);

        Despesa despesa = new Despesa(null, outroUsuario, "LAZER", 100.0, "cinema", 1, LocalDate.now(), null, false, true);
        despesa = despesaRepository.save(despesa);

        AtualizarDespesaDTO dto = new AtualizarDespesaDTO("LAZER", 150.0, "cinema atualizado", 1, LocalDate.now());

        mockMvc.perform(put(BASE_URL + "/" + despesa.getId())
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Deve retornar 404 ao tentar atualizar uma despesa inexistente")
    void deveRetornar404AoAtualizarDespesaInexistente() throws Exception {
        AtualizarDespesaDTO dto = new AtualizarDespesaDTO("OUTROS", 100.0, "tentativa", 1, LocalDate.now());

        mockMvc.perform(put(BASE_URL + "/999999")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Deve retornar 400 ao tentar atualizar com dados inválidos")
    void deveRetornar400AoAtualizarComDadosInvalidos() throws Exception {
        Despesa despesa = new Despesa(null, usuario, "SAÚDE", 200.0, "remédio", 1, LocalDate.now(), null, false, true);
        despesa = despesaRepository.save(despesa);

        AtualizarDespesaDTO dto = new AtualizarDespesaDTO("", -10.0, "", 0, LocalDate.now().plusDays(1));

        mockMvc.perform(put(BASE_URL + "/" + despesa.getId())
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Deve listar apenas despesas ativas quando não usar filtros")
    void deveListarApenasDespesasAtivasSemFiltro() throws Exception {

        Despesa despesaAtiva1 = new Despesa(null, usuario, "TRANSPORTE", 100.0, "ônibus", 1,
                LocalDate.now(), null, false, true);
        Despesa despesaAtiva2 = new Despesa(null, usuario, "ALIMENTAÇÃO", 50.0, "almoço", 1,
                LocalDate.now(), null, false, true);
        Despesa despesaInativa = new Despesa(null, usuario, "LAZER", 200.0, "cinema", 1,
                LocalDate.now(), null, false, false);

        despesaRepository.saveAll(List.of(despesaAtiva1, despesaAtiva2, despesaInativa));

        mockMvc.perform(get(BASE_URL)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(2))
                .andExpect(jsonPath("$.content[?(@.ativo == false)]").doesNotExist());
    }

    @Test
    @DisplayName("Deve listar apenas despesas ativas quando usar filtro")
    void deveListarDespesasAtivasComFiltroCategoria() throws Exception {
        Despesa despesaAtiva1 = new Despesa(null, usuario, "TRANSPORTE", 100.0, "ônibus", 1,
                LocalDate.of(2025, 6, 1), null, false, true);
        Despesa despesaAtiva2 = new Despesa(null, usuario, "TRANSPORTE", 50.0, "metrô", 1,
                LocalDate.of(2025, 6, 10), null, false, true);
        Despesa despesaInativa = new Despesa(null, usuario, "TRANSPORTE", 30.0, "táxi", 1,
                LocalDate.of(2025, 6, 5), null, false, false);

        despesaRepository.saveAll(List.of(despesaAtiva1, despesaAtiva2, despesaInativa));

        mockMvc.perform(get(BASE_URL)
                        .param("categoria", "TRANSPORTE")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.despesas.length()").value(2))
                .andExpect(jsonPath("$.despesas[?(@.ativo == false)]").doesNotExist())
                .andExpect(jsonPath("$.despesas[*].categoria").value(
                        Matchers.everyItem(Matchers.equalTo("TRANSPORTE"))));
    }

    @Test
    @DisplayName("Deve listar todas as despesas sem filtro, porém páginadas")
    void deveListarTodasSemFiltrosComSucesso200() throws Exception {
        mockMvc.perform(get(BASE_URL)
                .header("Authorization", "Bearer " + this.token))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Deve retornar as despesas com o filtro e status 200")
    void deveListarAsDespesasFiltradasComSucesso200() throws Exception {
        mockMvc.perform(get(BASE_URL)
                .param("categoria", "TRANSPORTE")
                .header("Authorization", "Bearer " + this.token))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Deve retornar erro 400 ao tentar filtar a despesa")
    void deveRetornar400AoListarDespesaComFiltroErrado() throws Exception {
        mockMvc.perform(get(BASE_URL)
                .param("categoria", "MERCADO")
                .param("dataInicio", "data invalida")
                .header("Authorization", "Bearer " + this.token))
                .andExpect(status().isBadRequest());
                
    }

    @Test
    @DisplayName("Deve retornar o erro 401 ao tentar listar as despesas com um token inválido")
    void deveRetornar401AoListarDespesaComUmTokenInvalido() throws Exception {
        mockMvc.perform(get(BASE_URL)
                .header("Authorization", "Bearer token.invalido"))
                .andExpect(status().isUnauthorized());
    }

    @DisplayName("Safe delete: Deve inativar uma despesa com sucesso e retornar 200")
    @Test
    void deveInativarUmaDespesaComSucesso() throws Exception {
        NovaDespesaDTO novaDespesa = new NovaDespesaDTO("SAÚDE", 300.0, "remédios", 1, LocalDate.now(), null, false, true);

        MvcResult result = mockMvc.perform(post(BASE_URL)
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(novaDespesa)))
                .andExpect(status().isCreated())
                .andReturn();

        String locationHeader = result.getResponse().getHeader("location");
        Long idDespesa = Long.valueOf(locationHeader.substring(locationHeader.lastIndexOf("/") + 1));

        mockMvc.perform(patch(BASE_URL + "/" + idDespesa + "/inativar")
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());

        Despesa despesa = despesaRepository.findById(idDespesa).orElseThrow();
        assertFalse(despesa.getAtivo());
    }

    @DisplayName("Safe delete: Deve retornar 400 ao tentar inativar despesa inexistente ou de outro usuário")
    @Test
    void deveRetornar400AoInativarUmaDespesaInexistente() throws Exception {
        mockMvc.perform(patch(BASE_URL + "/99999/inativar")
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isBadRequest());
    }

    @DisplayName("Safe delete: Deve retornar 401 ao tentar inativar uma despesa com token inválido")
    @Test
    void deveRetornar401AoInativarUmaDespesaComTokenInvalido() throws Exception {
        mockMvc.perform(patch(BASE_URL + "/1/inativar")
                .header("Authorization", "Bearer token.invalido"))
                .andExpect(status().isUnauthorized());
    }

}
