package com.github.progirls.despesas.api.despesas_api.service;

import com.github.progirls.despesas.api.despesas_api.dto.DespesaDTO;
import com.github.progirls.despesas.api.despesas_api.dto.NovaDespesaDTO;
import com.github.progirls.despesas.api.despesas_api.dto.UsuarioDTO;
import com.github.progirls.despesas.api.despesas_api.entities.Despesa;
import com.github.progirls.despesas.api.despesas_api.entities.Usuario;
import com.github.progirls.despesas.api.despesas_api.repository.DespesaRepository;
import com.github.progirls.despesas.api.despesas_api.repository.UsuarioRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class DespesaService {

    private final DespesaRepository despesaRepository;
    private final UsuarioRepository usuarioRepository;

    public DespesaService(DespesaRepository despesaRepository, UsuarioRepository usuarioRepository) {
        this.despesaRepository = despesaRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public DespesaDTO criarDespesa(NovaDespesaDTO dto, Authentication authentication) {
        Despesa despesa = new Despesa();
        despesa.setDescricao(dto.descricao());
        despesa.setValor(dto.valor());
        despesa.setParcelamento(dto.parcelamento());
        despesa.setDataInicio(dto.dataInicio());

        if (dto.parcelamento() == 1) {
            despesa.setDataFim(dto.dataInicio());
        } else {
            despesa.setDataFim(calcularDataFim(dto.dataInicio(), dto.parcelamento()));
        }

        despesa.setQuitado(quitadoOuFalso(dto));

        String email = authentication.getName();
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));
        despesa.setUsuario(usuario);

        Despesa salva = despesaRepository.save(despesa);
        UsuarioDTO usuarioDTO = new UsuarioDTO(usuario.getNome(), usuario.getEmail(), usuario.getDataCriacao());

        return new DespesaDTO(
                salva.getId(),
                usuarioDTO,
                salva.getValor(),
                salva.getDescricao(),
                salva.getParcelamento(),
                salva.getDataInicio(),
                salva.getDataFim(),
                salva.getQuitado()
        );
    }

    private LocalDate calcularDataFim(LocalDate dataInicio, int parcelamento) {
        return dataInicio.plusMonths(parcelamento);
    }

    private Boolean quitadoOuFalso(NovaDespesaDTO dto) {
        return dto.quitado() != null ? dto.quitado() : false;
    }

}
