package com.github.progirls.despesas.api.despesas_api.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;

import com.github.progirls.despesas.api.despesas_api.dto.DespesaRegisterDto;
import com.github.progirls.despesas.api.despesas_api.entities.Despesa;
import com.github.progirls.despesas.api.despesas_api.entities.Usuario;
import com.github.progirls.despesas.api.despesas_api.mapper.DespesaMapper;
import com.github.progirls.despesas.api.despesas_api.repository.DespesaRepository;
import com.github.progirls.despesas.api.despesas_api.repository.UsuarioRepository;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Service
public class DespesaService {

    private final UsuarioRepository usuarioRepository;
    private final DespesaRepository despesaRepository;
    private final FindUser findUser;
    private final DespesaMapper despesaMapper;

    @Transactional
    public Despesa adicionarDespesa(DespesaRegisterDto dto, JwtAuthenticationToken token) {
        var usuarioLogado = usuarioRepository.findByEmail(token.getName());

        Despesa novaDespesa = new Despesa();
        novaDespesa.setUsuario(usuarioLogado.get());
        novaDespesa.setValor(dto.valor());
        novaDespesa.setDescricao(dto.descricao());
        novaDespesa.setParcelamento(dto.parcelamento());
        novaDespesa.setQuitado(dto.quitado());

        if (dto.dataInicio() != null) {
            novaDespesa.setDataInicio(dto.dataInicio());
        } else {
            novaDespesa.setDataInicio(LocalDateTime.now());
        }

        if (dto.parcelamento() != null && dto.parcelamento() > 1) {
            LocalDateTime dataBase = dto.dataInicio() != null ? dto.dataInicio() : LocalDateTime.now();
            novaDespesa.setDataInicio(dataBase);
            novaDespesa.setDataFim(previsaoDeTermino(dataBase, dto.parcelamento()));
        } else {
            novaDespesa.setDataInicio(dto.dataInicio() != null ? dto.dataInicio() : LocalDateTime.now());
            novaDespesa.setDataFim(null);
        }
        

        return despesaRepository.save(novaDespesa);
    }


    private LocalDateTime previsaoDeTermino(LocalDateTime dataInicio, int parcelamento) {
        return dataInicio.plusMonths(parcelamento - 1L);
    }
    

    public List<Despesa> listarDespesas() {
        return despesaRepository.findAll();
    }
}
