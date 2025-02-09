package com.progweb.siri_cascudo_api.dto.Auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserLoginDTO {
    @NotBlank(message = "O email é obrigatório")
    @Email(message = "Email inválido")
    private String email;

    @NotBlank(message = "A senha é obrigatória")
    private String password;
}
