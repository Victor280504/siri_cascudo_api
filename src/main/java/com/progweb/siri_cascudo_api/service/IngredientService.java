package com.progweb.siri_cascudo_api.service;

import com.progweb.siri_cascudo_api.dto.CreateResponseDTO;
// import com.progweb.siri_cascudo_api.dto.DeleteResponseDTO;
import com.progweb.siri_cascudo_api.dto.Ingredient.CreateIngredientDTO;
import com.progweb.siri_cascudo_api.dto.Ingredient.IngredientDTO;
import com.progweb.siri_cascudo_api.dto.UpdateResponseDTO;
import com.progweb.siri_cascudo_api.exception.ResourceNotFoundException;
import com.progweb.siri_cascudo_api.model.Ingredient;
import com.progweb.siri_cascudo_api.repository.IngredientRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class IngredientService {
    @Autowired
    private IngredientRepository ingredientRepository;

    public CreateResponseDTO createIngredient(CreateIngredientDTO dto) {
        Ingredient ingredient = new Ingredient();
        ingredient.setDescription(dto.getDescription());
        ingredient.setQuantity(dto.getQuantity());
        ingredient.setPrice(dto.getPrice());
        Ingredient savedIngredient = ingredientRepository.save(ingredient);
        return new CreateResponseDTO(savedIngredient.getId().toString(), "Ingrediente criado com sucesso!");
    }

    public IngredientDTO getIngredient(Long id) {
        Ingredient ingredient = ingredientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ingrediente não encontrado."));
        return new IngredientDTO(ingredient.getId(), ingredient.getQuantity(), ingredient.getDescription(), ingredient.getPrice());
    }

    public List<IngredientDTO> getAllIngredients() {
        return ingredientRepository.findAll()
                .stream()
                .map(ingredient -> new IngredientDTO(ingredient.getId(), ingredient.getQuantity(), ingredient.getDescription(), ingredient.getPrice()))
                .collect(Collectors.toList());
    }

    public UpdateResponseDTO<IngredientDTO> updateIngredient(Long id, IngredientDTO dto) {
        Ingredient ingredient = ingredientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ingredient não encontrado."));

        if (dto.getDescription() != null && !dto.getDescription().equals(ingredient.getDescription())) {
            ingredient.setDescription(dto.getDescription());
        }

        if (dto.getQuantity() != null && !dto.getQuantity().equals(ingredient.getQuantity())) {
            ingredient.setQuantity(dto.getQuantity());
        }
        if (dto.getPrice() > 0 && dto.getPrice() != ingredient.getPrice()) {
            ingredient.setPrice(dto.getPrice());
        }

        ingredientRepository.save(ingredient);
        return new UpdateResponseDTO<>(ingredient.getId().toString(), "Ingrediente atualizado com sucesso!", mapToIngreditDTO(ingredient));
    }

    public CreateResponseDTO deleteIngredient(Long id) {
        Ingredient ingredient = ingredientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ingrediente não econtrado"));
        ingredientRepository.delete(ingredient);
        return new CreateResponseDTO(id.toString(), "Ingrediente deletado com sucesso!");
    }

    private IngredientDTO mapToIngreditDTO(Ingredient ingredient) {
        IngredientDTO ingredientDTO = new IngredientDTO();
        ingredientDTO.setId(ingredient.getId());
        ingredientDTO.setQuantity(ingredient.getQuantity());
        ingredientDTO.setDescription(ingredient.getDescription());
        ingredientDTO.setPrice(ingredient.getPrice());
        return ingredientDTO;
    }
}