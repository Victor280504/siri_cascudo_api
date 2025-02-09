package com.progweb.siri_cascudo_api.exception;

import lombok.Data;

@Data
public class ValidationError {
    private String field; // Nome do campo inválido
    private String message; // Mensagem de erro

    // Construtor
    public ValidationError(String field, String message) {
        this.field = field;
        this.message = message;
    }
}