package com.github.progirls.despesas.api.despesas_api.mapper;

import com.github.progirls.despesas.api.despesas_api.dto.*;
import com.github.progirls.despesas.api.despesas_api.entities.Despesa;
import com.github.progirls.despesas.api.despesas_api.entities.Usuario;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface DespesaMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "usuario", ignore = true)
    Despesa toEntity(NovaDespesaDTO dto);

    @Mapping(source = "usuario", target = "usuario")
    DespesaDTO toDTO(Despesa despesa);

    DespesaFiltradaDTO toFiltradaDTO(Despesa despesa);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void atualizarDespesa(@MappingTarget Despesa despesa, AtualizarDespesaDTO dto);

    UsuarioDTO toUsuarioDTO(Usuario usuario);
}
