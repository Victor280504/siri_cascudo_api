package com.progweb.siri_cascudo_api.controller;

import com.progweb.siri_cascudo_api.dto.ProductDTO;
import com.progweb.siri_cascudo_api.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.validation.BindingResult;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.access.prepost.PreAuthorize;

@RestController
@RequestMapping("/api/products")
@CrossOrigin(origins = "*")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    // Qualquer usuário pode acessar produtos (não autenticado)
    @GetMapping
    public List<ProductDTO> getAllProducts() {
        return productService.getAllProducts();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getProductById(@PathVariable Long id) {
        return ResponseEntity.ok(productService.getProductById(id));
    }

    // Apenas ADMIN pode adicionar produtos
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> createProduct(
            @Valid @RequestParam String name,
            @RequestParam String description,
            @RequestParam Double price,
            @RequestParam int quantity,
            @RequestParam Long idCategory,
            @RequestParam("image") MultipartFile image,
            BindingResult bindingResult) throws IOException {

        if (bindingResult.hasErrors()) {
            List<String> errors = bindingResult.getFieldErrors().stream()
                    .map(error -> error.getField() + ": " + error.getDefaultMessage())
                    .collect(Collectors.toList());
            return ResponseEntity.badRequest().body(errors);
        }

        ProductDTO product = productService.createProduct(name, description, price, quantity, idCategory, image);
        ProductDTO product = productService.createProduct(name, description, price, idCategory, image);
        return ResponseEntity.status(HttpStatus.CREATED).body(product);
    }

    // Apenas ADMIN pode atualizar produtos
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProductDTO> updateProduct(
            @PathVariable Long id,
            @RequestParam String name,
            @RequestParam String description,
            @RequestParam Double price,
            @RequestParam int quantity,
            @RequestParam Long idCategory,
            @RequestParam(value = "image", required = false) MultipartFile image) throws IOException {

        ProductDTO updatedProduct = productService.updateProduct(id, name, description, price, quantity, idCategory, image);
        return ResponseEntity.ok(updatedProduct);
    }

    // Apenas ADMIN pode deletar produtos
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.ok("Produto deletado com sucesso!");
    }
}
