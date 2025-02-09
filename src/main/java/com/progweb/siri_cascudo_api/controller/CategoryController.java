package com.progweb.siri_cascudo_api.controller;

import com.progweb.siri_cascudo_api.dto.CategoryDTO;
import com.progweb.siri_cascudo_api.dto.CreateResponseDTO;
import com.progweb.siri_cascudo_api.dto.UpdateResponseDTO;
import com.progweb.siri_cascudo_api.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
// import org.springframework.validation.BindingResult;
import org.springframework.http.HttpStatus;

// import java.io.IOException;
import java.util.List;
// import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/categories")
@CrossOrigin(origins = "*")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public List<CategoryDTO> getAllCategories() {
        return categoryService.getAllCategories();
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryDTO> getCategoryById(@PathVariable Long id) {
        return ResponseEntity.ok(categoryService.getCategoryById(id));
    }

    @PostMapping
    public ResponseEntity<CreateResponseDTO> createCategory(@Valid @RequestBody CategoryDTO categoryDTO) {
        CreateResponseDTO response = categoryService.createCategory(categoryDTO.getName());
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UpdateResponseDTO<CategoryDTO>> updateCategory(
        @PathVariable Long id, 
        @Valid @RequestBody CategoryDTO categoryDTO) {
            UpdateResponseDTO<CategoryDTO> response = categoryService.updateCategory(id, categoryDTO);
            return new ResponseEntity<>(response, HttpStatus.OK);
    }   

    @DeleteMapping("/{id}")
    public ResponseEntity<CreateResponseDTO> deleteCategory(
        @PathVariable Long id) {
            categoryService.deleteCategory(id);
            CreateResponseDTO response = new CreateResponseDTO(id.toString(), "Categoria deletada com sucesso.");
            return ResponseEntity.ok(response);
    }
}
