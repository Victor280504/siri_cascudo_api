package com.progweb.siri_cascudo_api.exception.responses;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ErrorResponse {
    private LocalDateTime timestamp;
    private int status;
    private String error;
    private String message;
    private String details;
    private String path;

    // Construtor
    public ErrorResponse(LocalDateTime timestamp, int status, String error, String message, String details, String path) {
        this.timestamp = timestamp;
        this.status = status;
        this.error = error;
        this.message = message;
        this.details = details;
        this.path = path;
    }
}