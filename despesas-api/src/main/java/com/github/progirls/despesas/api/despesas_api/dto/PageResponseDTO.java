package com.github.progirls.despesas.api.despesas_api.dto;

import java.util.List;

import org.springframework.data.domain.Page;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PageResponseDTO<T> {
    
    private List<T> content;
    private int pagina;
    private int tamanho;
    private int paginasTotais;
    private boolean primeira;
    private boolean ultima;
    
    public PageResponseDTO(Page<T> page) {
        this.content = page.getContent();
        this.pagina = page.getNumber();
        this.tamanho = page.getSize();
        this.paginasTotais = page.getTotalPages();
        this.primeira = page.isFirst();
        this.ultima = page.isLast();
    }
}
