package com.github.progirls.despesas.api.despesas_api.service;

import com.github.progirls.despesas.api.despesas_api.dto.AtualizarDespesaDTO;
import com.github.progirls.despesas.api.despesas_api.dto.DespesaDTO;
import com.github.progirls.despesas.api.despesas_api.dto.NovaDespesaDTO;
import com.github.progirls.despesas.api.despesas_api.entities.Despesa;
import com.github.progirls.despesas.api.despesas_api.entities.Usuario;
import com.github.progirls.despesas.api.despesas_api.mapper.DespesaMapper;
import com.github.progirls.despesas.api.despesas_api.repository.DespesaRepository;
import com.github.progirls.despesas.api.despesas_api.repository.UsuarioRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.time.LocalDate;

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

    @Transactional
    public Page<DespesaDTO> listarDespesaDoUsuario(Authentication authentication, Pageable pageable) {

        String email = authentication.getName();
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado."));

        return despesaRepository.findByUsuario(usuario, pageable)
                .map(despesaMapper::toDTO);
    }

    @Transactional
    public DespesaDTO editarDespesa(Authentication authentication, Long despesaId, AtualizarDespesaDTO atualizarDespesa) throws AccessDeniedException {
        String email = authentication.getName();
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario não encontrado."));

        Despesa despesa = despesaRepository.findById(despesaId)
                .orElseThrow(() -> new EntityNotFoundException("Despesa não encontrada."));

        if (!despesa.getUsuario().getId().equals(usuario.getId())) {
            throw new AccessDeniedException("Você não tem permissão para editar essa despesa.");
        }

        despesa.setValor(atualizarDespesa.valor());
        despesa.setDescricao(atualizarDespesa.descricao());
        despesa.setParcelamento(atualizarDespesa.parcelamento());
        despesa.setDataInicio(atualizarDespesa.dataInicio());

        if (atualizarDespesa.parcelamento() == 1) {
            despesa.setDataFim(atualizarDespesa.dataInicio());
        } else {
            despesa.setDataFim(atualizarDespesa.dataInicio().plusMonths(atualizarDespesa.parcelamento()));
        }

        return despesaMapper.toDTO(despesaRepository.save(despesa));
    }

    private LocalDate calcularDataFim(LocalDate dataInicio, int parcelamento) {
        return dataInicio.plusMonths(parcelamento);
    }

    private Boolean quitadoOuFalso(NovaDespesaDTO dto) {
        return dto.quitado() != null ? dto.quitado() : false;
    }

    
}
