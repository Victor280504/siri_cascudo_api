package com.progweb.siri_cascudo_api.service;

import com.progweb.siri_cascudo_api.dto.CreateResponseDTO;
import com.progweb.siri_cascudo_api.dto.RecipeDTO;
import com.progweb.siri_cascudo_api.exception.ResourceNotFoundException;
import com.progweb.siri_cascudo_api.model.Recipe;
import com.progweb.siri_cascudo_api.model.RecipeId;
import com.progweb.siri_cascudo_api.repository.RecipeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RecipeService {

    @Autowired
    private RecipeRepository recipeRepository;

    public List<RecipeDTO> getAllRecipes() {
        return recipeRepository.findAll()
                .stream()
                .map(this::mapToRecipeDTO)
                .collect(Collectors.toList());
    }

    public List<RecipeDTO> getRecipesByProductId(Long productId) {
        return recipeRepository.findById_IdProduct(productId) // Correção do nome do método
                .stream()
                .map(this::mapToRecipeDTO)
                .collect(Collectors.toList());
    }

    public CreateResponseDTO createRecipe(RecipeDTO recipeDTO) {
        RecipeId id = new RecipeId(recipeDTO.getIdProduct(), recipeDTO.getIdIngredient());

        if (recipeRepository.existsById(id)) {
            throw new IllegalArgumentException("Essa receita já existe.");
        }

        Recipe recipe = new Recipe();
        recipe.setId(id);
        recipe.setQuantity(recipeDTO.getQuantity());

        recipeRepository.save(recipe);
        return new CreateResponseDTO(id.toString(), "Receita criada com sucesso.");
    }

    public CreateResponseDTO deleteRecipe(Long idProduct, Long idIngredient) {
        RecipeId id = new RecipeId(idProduct, idIngredient);

        Recipe recipe = recipeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Recipe", "id", id));

        recipeRepository.delete(recipe);
        return new CreateResponseDTO(id.toString(), "Receita deletada com sucesso.");
    }

    private RecipeDTO mapToRecipeDTO(Recipe recipe) {
        RecipeDTO recipeDTO = new RecipeDTO();
        recipeDTO.setIdProduct(recipe.getId().getIdProduct());
        recipeDTO.setIdIngredient(recipe.getId().getIdIngredient());
        recipeDTO.setQuantity(recipe.getQuantity());
        return recipeDTO;
    }
}