package com.github.progirls.despesas.api.despesas_api.exception;

import java.time.LocalDateTime;
import java.util.List;

public class ErrorResponse {

    private Integer status;
    private String mensagem;
    private LocalDateTime timestamp;
    private List<String> detalhes; // Opcional, pode ser null para erros simples

    public ErrorResponse() {}

    public ErrorResponse(Integer status, String mensagem, LocalDateTime timestamp, List<String> detalhes) {
        this.status = status;
        this.mensagem = mensagem;
        this.timestamp = timestamp;
        this.detalhes = detalhes;
    }

    // Getters e Setters
    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public List<String> getDetalhes() {
        return detalhes;
    }

    public void setDetalhes(List<String> detalhes) {
        this.detalhes = detalhes;
    }
}