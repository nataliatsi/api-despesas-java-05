package com.github.progirls.despesas.api.despesas_api.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.github.progirls.despesas.api.despesas_api.dto.DespesaRegisterDto;
import com.github.progirls.despesas.api.despesas_api.dto.DespesaResponseDto;
import com.github.progirls.despesas.api.despesas_api.entities.Despesa;

@Mapper(componentModel = "spring")
public interface DespesaMapper {

    @Mapping(target = "usuario", ignore = true)
    @Mapping(target = "id", ignore = true)
    Despesa toDespesa(DespesaRegisterDto dto);
    DespesaResponseDto toResponseDto(Despesa entidade);

    
}