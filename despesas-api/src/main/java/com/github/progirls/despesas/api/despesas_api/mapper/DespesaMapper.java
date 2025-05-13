package com.github.progirls.despesas.api.despesas_api.mapper;

import com.github.progirls.despesas.api.despesas_api.dto.DespesaDTO;
import com.github.progirls.despesas.api.despesas_api.dto.DespesaFiltradaDTO;
import com.github.progirls.despesas.api.despesas_api.dto.NovaDespesaDTO;
import com.github.progirls.despesas.api.despesas_api.dto.UsuarioDTO;
import com.github.progirls.despesas.api.despesas_api.entities.Despesa;
import com.github.progirls.despesas.api.despesas_api.entities.Usuario;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface DespesaMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "usuario", ignore = true)
    Despesa toEntity(NovaDespesaDTO dto);

    @Mapping(source = "usuario", target = "usuario")
    DespesaDTO toDTO(Despesa despesa);

    DespesaFiltradaDTO toFiltradaDTO(Despesa despesa);

    UsuarioDTO toUsuarioDTO(Usuario usuario);
}
