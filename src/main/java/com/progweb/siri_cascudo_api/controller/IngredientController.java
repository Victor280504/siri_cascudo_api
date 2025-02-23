package com.progweb.siri_cascudo_api.controller;

import com.progweb.siri_cascudo_api.dto.CreateResponseDTO;
// import com.progweb.siri_cascudo_api.dto.DeleteResponseDTO;
import com.progweb.siri_cascudo_api.dto.Ingredient.CreateIngredientDTO;
import com.progweb.siri_cascudo_api.dto.Ingredient.IngredientDTO;
import com.progweb.siri_cascudo_api.dto.UpdateResponseDTO;
import com.progweb.siri_cascudo_api.service.IngredientService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@RestController
@RequestMapping("/api/ingredients")
@CrossOrigin(origins = "*")
public class IngredientController
{
    @Autowired
    private IngredientService ingredientService;

    @PostMapping
    public ResponseEntity<CreateResponseDTO> createIngredient(@Valid @RequestBody CreateIngredientDTO ingredientDTO) {
        return ResponseEntity.ok(ingredientService.createIngredient(ingredientDTO));
    }

    @GetMapping("/{id}")
    public ResponseEntity<IngredientDTO> getIngredient(@PathVariable Long id) {
        return ResponseEntity.ok(ingredientService.getIngredient(id));
    }

    @SuppressWarnings("rawtypes")
    @PutMapping("/{id}")
    public ResponseEntity<UpdateResponseDTO> updateIngredient(@PathVariable Long id, @Valid @RequestBody IngredientDTO ingredientDTO) {
        return ResponseEntity.ok(ingredientService.updateIngredient(id, ingredientDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<CreateResponseDTO> deleteIngredient(@PathVariable Long id) {
        return ResponseEntity.ok(ingredientService.deleteIngredient(id));
    }

    @GetMapping
    public ResponseEntity<List<IngredientDTO>> getAllIngredients() {
        return ResponseEntity.ok(ingredientService.getAllIngredients());
    }
}