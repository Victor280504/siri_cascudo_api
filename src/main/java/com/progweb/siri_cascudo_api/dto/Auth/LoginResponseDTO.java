package com.progweb.siri_cascudo_api.dto.Auth;

import lombok.Data;

@Data
public class LoginResponseDTO {
    private String id;
    private String token;

    // Construtor
    public LoginResponseDTO(String id, String token) {
        this.id = id;
        this.token = token;
    }
}