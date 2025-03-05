package com.progweb.siri_cascudo_api.dto;
import com.progweb.siri_cascudo_api.model.Ingredient;
import lombok.Data;

@Data
public class RecipeWithIngredientDTO {
    private Long idProduct;
    private Ingredient ingredient;
    private Integer quantity;
}
