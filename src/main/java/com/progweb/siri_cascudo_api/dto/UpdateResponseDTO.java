package com.progweb.siri_cascudo_api.dto;

import lombok.Data;

@Data
public class UpdateResponseDTO<T> {
    private String id;
    private String message;
    private T data;

    public UpdateResponseDTO(String id, String message, T data) {
        this.id = id;
        this.message = message;
        this.data = data;
    }
}
