package com.progweb.siri_cascudo_api.dto;

import lombok.Data;

@Data
public class CreateResponseDTO {
    private String id;
    private String message;

    public CreateResponseDTO(String id, String message) {
        this.id = id;
        this.message = message;
    }
}
