package com.progweb.siri_cascudo_api.service;

import com.progweb.siri_cascudo_api.dto.CreateResponseDTO;
import com.progweb.siri_cascudo_api.dto.UpdateResponseDTO;
import com.progweb.siri_cascudo_api.dto.CategoryDTO;
import com.progweb.siri_cascudo_api.exception.ResourceNotFoundException;
import com.progweb.siri_cascudo_api.model.Category;
import com.progweb.siri_cascudo_api.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    public List<CategoryDTO> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        return categories.stream()
                .map(this::mapToCategoryDTO)
                .collect(Collectors.toList());
    }

    public CategoryDTO getCategoryById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", id));
        return mapToCategoryDTO(category);
    }

    public CreateResponseDTO createCategory(String name) {
        Category category = new Category();
        category.setName(name);
        Category savedCategory = categoryRepository.save(category);
        return new CreateResponseDTO(savedCategory.getId().toString(), "Categoria criada com sucesso.");
    }


    public UpdateResponseDTO<CategoryDTO> updateCategory(Long id, CategoryDTO categoryDTO) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoria não encontrada"));
    
        category.setName(categoryDTO.getName());
        Category updatedCategory = categoryRepository.save(category);
    
        CategoryDTO updatedCategoryDTO = new CategoryDTO(updatedCategory.getId(), updatedCategory.getName());
    
        return new UpdateResponseDTO<>(
                updatedCategory.getId().toString(),
                "Categoria atualizada com sucesso.",
                updatedCategoryDTO
        );
    }

    public CreateResponseDTO deleteCategory(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", id));
        
        categoryRepository.delete(category);
        return new CreateResponseDTO(id.toString(), "Categoria deletada com sucesso.");
    }

    // Método auxiliar para converter Category em CategoryDTO
    private CategoryDTO mapToCategoryDTO(Category category) {
        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setId(category.getId());
        categoryDTO.setName(category.getName());
        return categoryDTO;
    }
}
