package com.progweb.siri_cascudo_api.dto.Sale;

import com.progweb.siri_cascudo_api.model.Ingredient;
import lombok.Data;

@Data
public class RecipeWithExpensesDTO {
    private Long idProduct;

    private Ingredient ingredient;

    private double expenses;

    private Integer quantity;
}
