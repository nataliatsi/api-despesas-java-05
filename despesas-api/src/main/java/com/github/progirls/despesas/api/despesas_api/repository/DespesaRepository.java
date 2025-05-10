package com.github.progirls.despesas.api.despesas_api.repository;

import com.github.progirls.despesas.api.despesas_api.entities.Despesa;
import com.github.progirls.despesas.api.despesas_api.entities.Usuario;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DespesaRepository extends JpaRepository<Despesa, Long> {
    Page<Despesa> findByUsuario(Usuario usuario, Pageable pageable);
}
