package com.github.progirls.despesas.api.despesas_api.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.github.progirls.despesas.api.despesas_api.dto.UsuarioRedefinirSenhaDTO;
import com.github.progirls.despesas.api.despesas_api.entities.Usuario;
import com.github.progirls.despesas.api.despesas_api.repository.UsuarioRepository;

@ExtendWith(MockitoExtension.class)
public class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private UsuarioService usuarioService;

    private Usuario usuario;
    private UsuarioRedefinirSenhaDTO novSenhaDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        usuario = new Usuario();
        usuario.setNome("Teste de senha");
        usuario.setEmail("usuario@teste.com");
        usuario.setSenha("Senha@Ant1ga");

        novSenhaDTO = new UsuarioRedefinirSenhaDTO("Teste@1234");
    }

    @DisplayName("Deve atualizar a senha do usuário com sucesso")
    @Test
    void deveAtualizarSenhaComSucesso() {
        when(usuarioRepository.findByEmail(usuario.getEmail())).thenReturn(java.util.Optional.of(usuario));
        when(bCryptPasswordEncoder.encode(novSenhaDTO.senha())).thenReturn("senhaCriptografada");
        when(authentication.getName()).thenReturn(usuario.getEmail());

        usuarioService.atualizarSenha(novSenhaDTO, authentication);
        assertEquals("senhaCriptografada", usuario.getSenha());

        verify(usuarioRepository, times(1)).save(usuario);
    }

    @DisplayName("Deve falhar ao tentar atualizar a senha sem um token válido")
    @Test
    void deveFalharAoTentarAtualizarSenhaSemTokenValido() {
        when(usuarioRepository.findByEmail(usuario.getEmail())).thenReturn(java.util.Optional.empty());
        when(authentication.getName()).thenReturn(usuario.getEmail());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            usuarioService.atualizarSenha(novSenhaDTO, authentication);
        });

        assertEquals("Usuário não encontrado", exception.getMessage());
    }
}
