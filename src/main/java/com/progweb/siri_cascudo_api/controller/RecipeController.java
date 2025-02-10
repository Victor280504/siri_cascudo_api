package com.progweb.siri_cascudo_api.controller;

import com.progweb.siri_cascudo_api.dto.CreateResponseDTO;
import com.progweb.siri_cascudo_api.dto.RecipeDTO;
import com.progweb.siri_cascudo_api.service.RecipeService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recipes")
@CrossOrigin(origins = "*")
public class RecipeController {

    private final RecipeService recipeService;

    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @GetMapping
    public List<RecipeDTO> getAllRecipes() {
        return recipeService.getAllRecipes();
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<List<RecipeDTO>> getRecipesByProductId(@PathVariable Long productId) {
        return ResponseEntity.ok(recipeService.getRecipesByProductId(productId));
    }

    @PostMapping
    public ResponseEntity<CreateResponseDTO> createRecipe(@Valid @RequestBody RecipeDTO recipeDTO) {
        CreateResponseDTO response = recipeService.createRecipe(recipeDTO);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{idProduct}/{idIngredient}")
    public ResponseEntity<CreateResponseDTO> deleteRecipe(@PathVariable Long idProduct, @PathVariable Long idIngredient) {
        CreateResponseDTO response = recipeService.deleteRecipe(idProduct, idIngredient);
        return ResponseEntity.ok(response);
    }
}