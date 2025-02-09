package com.progweb.siri_cascudo_api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class IngredientDTO
{
    private String id;

    @NotNull(message = "A quantidade de ingrediente não pode ser nula")
    private Integer quantity;

    @NotBlank(message = "A descrição para o ingrediente é obrigatória")
    private String description;
}