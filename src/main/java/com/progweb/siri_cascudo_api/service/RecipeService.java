package com.progweb.siri_cascudo_api.service;

import com.progweb.siri_cascudo_api.dto.CreateResponseDTO;
import com.progweb.siri_cascudo_api.dto.RecipeDTO;
import com.progweb.siri_cascudo_api.dto.RecipeWithIngredientDTO;
import com.progweb.siri_cascudo_api.dto.Sale.RecipeWithExpensesDTO;
import com.progweb.siri_cascudo_api.dto.UpdateResponseDTO;
import com.progweb.siri_cascudo_api.exception.CustomException;
import com.progweb.siri_cascudo_api.exception.ResourceNotFoundException;
import com.progweb.siri_cascudo_api.model.Ingredient;
import com.progweb.siri_cascudo_api.model.Recipe;
import com.progweb.siri_cascudo_api.model.RecipeId;
import com.progweb.siri_cascudo_api.repository.RecipeRepository;
import com.progweb.siri_cascudo_api.repository.ProductRepository;
import com.progweb.siri_cascudo_api.repository.IngredientRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
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

    public List<RecipeWithExpensesDTO> getRecipesWithExpensesByProductId(Long productId) {
        List<RecipeWithIngredientDTO> recipesWithoutExpenses = getRecipeWithIngredient(productId);

        return recipesWithoutExpenses.stream().map(this::mapToRecipeWithExpenses).toList();
    }

    public Double getTotalRecipeCost(List<RecipeWithExpensesDTO> recipes) {
        return recipes.stream().mapToDouble(RecipeWithExpensesDTO::getExpenses)
                .sum();
    }

    public Double getRecipeCostByProduct(Long productId) {
        List<RecipeWithExpensesDTO> recipes = getRecipesWithExpensesByProductId(productId);
        return getTotalRecipeCost(recipes);
    }

    public RecipeWithExpensesDTO mapToRecipeWithExpenses(RecipeWithIngredientDTO recipeWithIngredientDTOS) {
        RecipeWithExpensesDTO result = new RecipeWithExpensesDTO();
        result.setIngredient(recipeWithIngredientDTOS.getIngredient());
        result.setIdProduct(recipeWithIngredientDTOS.getIdProduct());
        result.setQuantity(recipeWithIngredientDTOS.getQuantity());
        result.setExpenses(recipeWithIngredientDTOS.getQuantity() * recipeWithIngredientDTOS.getIngredient().getPrice());
        return result;
    }

    public List<RecipeWithIngredientDTO> getRecipeWithIngredient(Long productId) {
        List<RecipeDTO> recipeDTOS = getRecipesByProductId(productId);
        List<Ingredient> ingredients = ingredientRepository.findAll();
        return recipeDTOS.stream()
                .map(recipeDTO -> {
                    Ingredient ingredient = findIngredientForRecipe(recipeDTO, ingredients);
                    RecipeWithIngredientDTO newDTO = new RecipeWithIngredientDTO();
                    newDTO.setIngredient(ingredient);
                    newDTO.setQuantity(recipeDTO.getQuantity());
                    newDTO.setIdProduct(recipeDTO.getIdProduct());
                    return newDTO;
                })
                .collect(Collectors.toList());
    }

    private Ingredient findIngredientForRecipe(RecipeDTO recipeDTO, List<Ingredient> ingredientes) {
        return ingredientes.stream()
                .filter(ingredient -> Objects.equals(ingredient.getId(), recipeDTO.getIdIngredient()))
                .findFirst()
                .orElse(null);
    }

    public CreateResponseDTO createRecipeByList(List<RecipeDTO> recipesDTO) {
        Long productID = recipesDTO.getFirst().getIdProduct();

        List<Recipe> recipes = recipesDTO.stream().map(this::mapToRecipe).toList();
        recipeRepository.saveAll(recipes);
        return new CreateResponseDTO(productID.toString(), "Receita criada com sucesso");
    }

    public UpdateResponseDTO<List<Recipe>> updateRecipeByList(Long productID, List<RecipeDTO> recipesDTO) {
        List<Recipe> recipes = recipesDTO.stream().map(this::mapToRecipe).toList();

        recipeRepository.saveAll(recipes);

        return new UpdateResponseDTO<>(productID.toString(), "Receita atualizada com sucesso", recipes);
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

    public int getAvailableQuantity(Long id) {
        List<RecipeDTO> recipes = getRecipesByProductId(id);
        List<Ingredient> ingredients = ingredientRepository.findAll();

        if (recipes.isEmpty()) {
            return 0; // Se não há receita, não podemos produzir nada
        }

        // Mapa para acessar os ingredientes do estoque mais rápido
        Map<Long, Integer> stockMap = ingredients.stream()
                .collect(Collectors.toMap(Ingredient::getId, Ingredient::getQuantity));

        int maxProducts = Integer.MAX_VALUE;

        for (RecipeDTO recipe : recipes) {
            Long ingredientId = recipe.getIdIngredient();
            int requiredQuantity = recipe.getQuantity();

            if (!stockMap.containsKey(ingredientId) || stockMap.get(ingredientId) < requiredQuantity) {
                return 0; // Se faltar algum ingrediente essencial, não podemos produzir nada
            }

            int available = stockMap.get(ingredientId) / requiredQuantity;
            maxProducts = Math.min(maxProducts, available);
        }
        return maxProducts;
    }

    private RecipeDTO mapToRecipeDTO(Recipe recipe) {
        RecipeDTO recipeDTO = new RecipeDTO();
        recipeDTO.setIdProduct(recipe.getId().getIdProduct());
        recipeDTO.setIdIngredient(recipe.getId().getIdIngredient());
        recipeDTO.setQuantity(recipe.getQuantity());
        return recipeDTO;
    }

    private Recipe mapToRecipe(RecipeDTO recipe) {
        Recipe newRecipe = new Recipe();
        RecipeId id = new RecipeId(recipe.getIdProduct(), recipe.getIdIngredient());
        newRecipe.setId(id);
        newRecipe.setQuantity(recipe.getQuantity());
        return newRecipe;
    }
}
