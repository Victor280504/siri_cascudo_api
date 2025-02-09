package com.progweb.siri_cascudo_api.service;

import com.progweb.siri_cascudo_api.dto.CreateResponseDTO;
// import com.progweb.siri_cascudo_api.dto.DeleteResponseDTO;
import com.progweb.siri_cascudo_api.dto.IngredientDTO;
import com.progweb.siri_cascudo_api.dto.UpdateResponseDTO;
import com.progweb.siri_cascudo_api.exception.ResourceNotFoundException;
import com.progweb.siri_cascudo_api.model.Ingredient;
import com.progweb.siri_cascudo_api.repository.IngredientRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class IngredientService
{
    @Autowired
    private IngredientRepository ingredientRepository;

    public CreateResponseDTO createIngredient(IngredientDTO dto)
    {
        Ingredient ingredient = new Ingredient();
        ingredient.setDescription(dto.getDescription());
        ingredient.setQuantity(dto.getQuantity());
        Ingredient savedIngredient = ingredientRepository.save(ingredient);
        return new CreateResponseDTO(savedIngredient.getId(), "Ingredient created successfully");
    }

    public IngredientDTO getIngredient(Long id)
    {
        Ingredient ingredient = ingredientRepository.findById(id.toString())
                .orElseThrow(() -> new ResourceNotFoundException("Ingrediente não encontrado"));
        return new IngredientDTO(ingredient.getId(), ingredient.getQuantity(), ingredient.getDescription());
    }

    public List<IngredientDTO> getAllIngredients()
    {
        return ingredientRepository.findAll()
                .stream()
                .map(ingredient -> new IngredientDTO(ingredient.getId(), ingredient.getQuantity(), ingredient.getDescription()))
                .collect(Collectors.toList());
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public UpdateResponseDTO updateIngredient(String id, IngredientDTO dto)
    {
        Ingredient ingredient = ingredientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ingredient not found"));
        ingredient.setQuantity(dto.getQuantity());
        ingredient.setDescription(dto.getDescription());
        ingredientRepository.save(ingredient);
        return new UpdateResponseDTO(ingredient.getId(), "Ingredient updated successfully", mapToIngreditDTO(ingredient));
    }

    public CreateResponseDTO deleteIngredient(String id)
    {
        Ingredient ingredient = ingredientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ingredient not found"));
        ingredientRepository.delete(ingredient);
        return new CreateResponseDTO(id, "Ingredient deleted successfully");
    }
    
    private IngredientDTO mapToIngreditDTO(Ingredient ingredient) {
        IngredientDTO ingredientDTO = new IngredientDTO();
        ingredientDTO.setId(ingredient.getId());
        ingredientDTO.setQuantity(ingredient.getQuantity());
        ingredientDTO.setDescription(ingredient.getDescription());
        return ingredientDTO;
    }
}