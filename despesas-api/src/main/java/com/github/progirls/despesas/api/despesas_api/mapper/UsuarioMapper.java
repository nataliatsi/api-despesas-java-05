package com.github.progirls.despesas.api.despesas_api.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.github.progirls.despesas.api.despesas_api.dto.DespesaRegisterDto;
import com.github.progirls.despesas.api.despesas_api.dto.GetDespesaDto;
import com.github.progirls.despesas.api.despesas_api.dto.GetUsuarioDto;
import com.github.progirls.despesas.api.despesas_api.dto.UsuarioRegisterDto;
import com.github.progirls.despesas.api.despesas_api.entities.Despesa;
import com.github.progirls.despesas.api.despesas_api.entities.Usuario;

@Mapper(componentModel = "spring")
public interface UsuarioMapper {
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "dataCriacao", ignore = true)
    @Mapping(target = "despesas", ignore = true)
    Usuario toUsuario(UsuarioRegisterDto usuarioRegister);
    GetUsuarioDto toDto(Usuario usuario);

}
