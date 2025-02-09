package com.progweb.siri_cascudo_api.exception;

import java.util.ArrayList;
import java.util.List;

public class ValidationException extends RuntimeException {
    private final List<ValidationError> errors; // Lista de erros de validação

    public ValidationException(List<ValidationError> errors) {
        super("Validation failed");
        this.errors = errors;
    }

    public List<ValidationError> getErrors() {
        return errors;
    }
}