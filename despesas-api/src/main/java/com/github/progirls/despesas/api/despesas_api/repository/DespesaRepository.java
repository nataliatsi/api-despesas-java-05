package com.github.progirls.despesas.api.despesas_api.repository;

import com.github.progirls.despesas.api.despesas_api.entities.Despesa;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DespesaRepository extends JpaRepository<Despesa, Long> {
}
