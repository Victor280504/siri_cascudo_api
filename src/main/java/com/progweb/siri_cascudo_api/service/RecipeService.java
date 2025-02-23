package com.progweb.siri_cascudo_api.service;

import com.progweb.siri_cascudo_api.dto.CreateResponseDTO;
import com.progweb.siri_cascudo_api.dto.RecipeDTO;
import com.progweb.siri_cascudo_api.exception.CustomException;
import com.progweb.siri_cascudo_api.exception.ResourceNotFoundException;
import com.progweb.siri_cascudo_api.model.Recipe;
import com.progweb.siri_cascudo_api.model.RecipeId;
import com.progweb.siri_cascudo_api.repository.RecipeRepository;
import com.progweb.siri_cascudo_api.repository.ProductRepository;
import com.progweb.siri_cascudo_api.repository.IngredientRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RecipeService {

    @Autowired
    private RecipeRepository recipeRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private IngredientRepository ingredientRepository;

    public List<RecipeDTO> getAllRecipes() {
        return recipeRepository.findAll()
                .stream()
                .map(this::mapToRecipeDTO)
                .collect(Collectors.toList());
    }

    public List<RecipeDTO> getRecipesByProductId(Long productId) {
        return recipeRepository.findById_IdProduct(productId)
                .stream()
                .map(this::mapToRecipeDTO)
                .collect(Collectors.toList());
    }

    public CreateResponseDTO createRecipe(RecipeDTO recipeDTO) {
        // Verifica se o produto existe
        productRepository.findById(recipeDTO.getIdProduct())
                .orElseThrow(
                        () -> new ResourceNotFoundException("Produto não encontrado", "id", recipeDTO.getIdProduct()));

        // Verifica se o ingrediente existe
        ingredientRepository.findById(recipeDTO.getIdIngredient())
                .orElseThrow(() -> new ResourceNotFoundException("Ingrediente não encontrado", "id",
                        recipeDTO.getIdIngredient()));

        // Cria a chave composta para a receita
        RecipeId id = new RecipeId(recipeDTO.getIdProduct(), recipeDTO.getIdIngredient());

        // Verifica se a receita já existe
        if (recipeRepository.existsById(id)) {
            throw new CustomException(HttpStatus.CONFLICT.value(), "Esta receita já foi cadastrada.", "Receita já encontrada no banco de dados, por favor cadastre uma nova receita diferente!");
        }

        // Cria e salva a nova receita
        Recipe recipe = new Recipe();
        recipe.setId(id);
        recipe.setQuantity(recipeDTO.getQuantity());

        recipeRepository.save(recipe);
        return new CreateResponseDTO(id.toString(), "Receita criada com sucesso.");
    }

    public CreateResponseDTO updateRecipeQuantity(Long idProduct, Long idIngredient, Integer newQuantity) {
        RecipeId id = new RecipeId(idProduct, idIngredient);

        Recipe recipe = recipeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Receita não encontrada", "id", id));

        recipe.setQuantity(newQuantity);
        recipeRepository.save(recipe);

        return new CreateResponseDTO(id.toString(), "Quantidade da receita atualizada com sucesso.");
    }

    public CreateResponseDTO deleteRecipeByRecipe(Long idProduct, Long idIngredient) {
        RecipeId id = new RecipeId(idProduct, idIngredient);

        Recipe recipe = recipeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Receita não encontrada", "id", id));

        recipeRepository.delete(recipe);
        return new CreateResponseDTO(id.toString(), "Receita deletada com sucesso.");
    }

    public CreateResponseDTO deleteRecipeByProduct(Long idProduct) {

        List<Recipe> recipe = recipeRepository.findById_IdProduct(idProduct);

        recipeRepository.deleteAll(recipe);
        return new CreateResponseDTO(idProduct.toString(), "Receita deletada com sucesso.");
    }

    private RecipeDTO mapToRecipeDTO(Recipe recipe) {
        RecipeDTO recipeDTO = new RecipeDTO();
        recipeDTO.setIdProduct(recipe.getId().getIdProduct());
        recipeDTO.setIdIngredient(recipe.getId().getIdIngredient());
        recipeDTO.setQuantity(recipe.getQuantity());
        return recipeDTO;
    }
}
