package com.github.progirls.despesas.api.despesas_api.service;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.cglib.core.Local;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;

import com.github.progirls.despesas.api.despesas_api.dto.DespesaRegisterDto;
import com.github.progirls.despesas.api.despesas_api.entities.Despesa;
import com.github.progirls.despesas.api.despesas_api.mapper.DespesaMapper;
import com.github.progirls.despesas.api.despesas_api.repository.DespesaRepository;
import com.github.progirls.despesas.api.despesas_api.repository.UsuarioRepository;

import jakarta.transaction.Transactional;

@Service
public class DespesaService {

    private final DespesaRepository despesaRepository;
    private final UsuarioRepository usuarioRepository;
    private final DespesaMapper despesaMapper;

    public DespesaService(DespesaRepository despesaRepository, UsuarioRepository usuarioRepository, DespesaMapper despesaMapper) {
        this.despesaRepository = despesaRepository;
        this.usuarioRepository = usuarioRepository;
        this.despesaMapper = despesaMapper;
    }

    @Transactional
    public Despesa adicionarDespesa(DespesaRegisterDto registerDto, JwtAuthenticationToken token) {

        var usuarioLogado = usuarioRepository.findByEmail(token.getName());

        Despesa novaDespesa = despesaMapper.toDespesa(registerDto);
        novaDespesa.setUsuario(usuarioLogado.get());
        novaDespesa.setValor(registerDto.valor());
        novaDespesa.setDescricao(registerDto.descricao());
        novaDespesa.setParcelamento(registerDto.parcelamento());
        novaDespesa.setQuitado(registerDto.quitado());
        
        
        if (registerDto.parcelamento() != null && registerDto.parcelamento() > 1) {
            LocalDate dataBase = registerDto.dataInicio() != null ? registerDto.dataInicio() : LocalDate.now();
            novaDespesa.setDataInicio(dataBase);
            novaDespesa.setDataFim(previsaoDeTermino(dataBase, registerDto.parcelamento()));
        } else {
            novaDespesa.setDataInicio(registerDto.dataInicio() != null ? registerDto.dataInicio() : LocalDate.now());
            novaDespesa.setDataFim(null);
        }
        

        novaDespesa = despesaRepository.save(novaDespesa);
        

        return novaDespesa;
    }

    private LocalDate previsaoDeTermino(LocalDate dataInicio, int parcelamento) {
        // A última parcela será 'parcelamento - 1' meses após a primeira
        return dataInicio.plusMonths(parcelamento - 1L);
    }

}
