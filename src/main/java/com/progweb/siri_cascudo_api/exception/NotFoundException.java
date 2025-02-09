package com.progweb.siri_cascudo_api.exception;

import org.springframework.http.HttpStatus;

public class NotFoundException extends CustomException {
    public NotFoundException(String message, String details) {
        super(HttpStatus.NOT_FOUND.value(), message, details);
    }
}