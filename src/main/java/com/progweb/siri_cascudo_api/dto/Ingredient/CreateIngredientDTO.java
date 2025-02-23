package com.progweb.siri_cascudo_api.dto.Ingredient;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateIngredientDTO {
    private Long id;

    @NotNull(message = "A quantidade de ingrediente não pode ser nula")
    private Integer quantity;

    @NotBlank(message = "A descrição para o ingrediente é obrigatória")
    private String description;

    @DecimalMin(value = "0.01", message = "O preço deve ser maior que zero.")
    private double price;
}