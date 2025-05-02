package com.github.progirls.despesas.api.despesas_api.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.github.progirls.despesas.api.despesas_api.dto.DespesaRegisterDto;
import com.github.progirls.despesas.api.despesas_api.dto.GetDespesaDto;
import com.github.progirls.despesas.api.despesas_api.entities.Despesa;

@Mapper(componentModel = "spring")
public interface DespesaMapper {
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "usuario", ignore = true)
    Despesa toDespesa(DespesaRegisterDto dRegisterDto);
    DespesaRegisterDto toRegisterDto(Despesa despesa);
}
