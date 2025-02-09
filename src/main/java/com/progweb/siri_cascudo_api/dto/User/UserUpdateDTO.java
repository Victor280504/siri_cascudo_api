package com.progweb.siri_cascudo_api.dto.User;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserUpdateDTO {
    @Size(min = 3, max = 50, message = "O nome deve ter entre 3 e 50 caracteres")
    private String name; // Opcional

    @Email(message = "Email inválido")
    private String email; // Opcional

    @Size(min = 6, message = "A senha deve ter pelo menos 6 caracteres")
    private String password; // Opcional

    private String address; // Opcional
}
