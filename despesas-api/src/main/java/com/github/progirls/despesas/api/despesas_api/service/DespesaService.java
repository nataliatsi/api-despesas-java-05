package com.github.progirls.despesas.api.despesas_api.service;

import com.github.progirls.despesas.api.despesas_api.dto.DespesaDTO;
import com.github.progirls.despesas.api.despesas_api.dto.DespesaFiltradaDTO;
import com.github.progirls.despesas.api.despesas_api.dto.NovaDespesaDTO;
import com.github.progirls.despesas.api.despesas_api.entities.Despesa;
import com.github.progirls.despesas.api.despesas_api.entities.DespesaSpecification;
import com.github.progirls.despesas.api.despesas_api.entities.Usuario;
import com.github.progirls.despesas.api.despesas_api.mapper.DespesaMapper;
import com.github.progirls.despesas.api.despesas_api.repository.DespesaRepository;
import com.github.progirls.despesas.api.despesas_api.repository.UsuarioRepository;
import com.github.progirls.despesas.api.despesas_api.security.AuthenticatedUserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class DespesaService {

    private final DespesaRepository despesaRepository;
    private final UsuarioRepository usuarioRepository;
    private final DespesaMapper despesaMapper;
    private final AuthenticatedUserService authenticatedUserService;

    public DespesaService(DespesaRepository despesaRepository, UsuarioRepository usuarioRepository, DespesaMapper despesaMapper, AuthenticatedUserService authenticatedUserService) {
        this.despesaRepository = despesaRepository;
        this.usuarioRepository = usuarioRepository;
        this.despesaMapper = despesaMapper;
        this.authenticatedUserService = authenticatedUserService;
    }

    public DespesaDTO criarDespesa(NovaDespesaDTO dto, Authentication authentication) {
        Despesa despesa = despesaMapper.toEntity(dto);

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
        return despesaMapper.toDTO(salva);
    }

    public List<DespesaFiltradaDTO> buscarDespesasComFiltros(
            String categoria,
            LocalDate dataInicio,
            LocalDate dataFim,
            Authentication authentication
    ) {
        Usuario usuario = authenticatedUserService.getUsuarioAutenticado(authentication);

        return despesaRepository
                .findAll(DespesaSpecification.comFiltros(usuario, categoria, dataInicio, dataFim))
                .stream()
                .map(despesaMapper::toFiltradaDTO)
                .toList();
    }

    private LocalDate calcularDataFim(LocalDate dataInicio, int parcelamento) {
        return dataInicio.plusMonths(parcelamento);
    }

    private Boolean quitadoOuFalso(NovaDespesaDTO dto) {
        return dto.quitado() != null ? dto.quitado() : false;
    }

}
