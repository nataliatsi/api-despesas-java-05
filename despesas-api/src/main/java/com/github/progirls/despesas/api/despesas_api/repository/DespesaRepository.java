package com.github.progirls.despesas.api.despesas_api.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.github.progirls.despesas.api.despesas_api.entities.Despesa;

public interface DespesaRepository extends JpaRepository<Despesa, Long>{
        
}
