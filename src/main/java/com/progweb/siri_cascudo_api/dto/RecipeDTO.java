package com.progweb.siri_cascudo_api.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RecipeDTO {
    @NotNull(message = "O ID do produto é obrigatório.")
    private Long idProduct;

    @NotNull(message = "O ID do ingrediente é obrigatório.")
    private Long idIngredient;

    @NotNull(message = "A quantidade é obrigatória.")
    @Min(value = 1, message = "A quantidade deve ser pelo menos 1.")
    private Integer quantity;
}
