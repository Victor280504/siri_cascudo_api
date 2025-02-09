package com.progweb.siri_cascudo_api.exception;

public class CustomException extends RuntimeException {
    private final int status; // Código de erro HTTP
    private final String details; // Detalhes personalizados

    public CustomException(int status, String message, String details) {
        super(message);
        this.status = status;
        this.details = details;
    }

    public int getStatus() {
        return status;
    }

    public String getDetails() {
        return details;
    }
}