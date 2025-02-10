package com.progweb.siri_cascudo_api.controller;

import com.progweb.siri_cascudo_api.dto.CreateResponseDTO;
import com.progweb.siri_cascudo_api.dto.Product.CreateProductDTO;
import com.progweb.siri_cascudo_api.dto.UpdateResponseDTO;
import com.progweb.siri_cascudo_api.dto.Product.ProductDTO;
import com.progweb.siri_cascudo_api.dto.Product.UpdateProductDTO;
import com.progweb.siri_cascudo_api.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@CrossOrigin(origins = "*")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    /**
     * Retorna a lista de todos os produtos disponíveis.
     * Acesso: Público (qualquer usuário pode visualizar).
     */
    @GetMapping
    public ResponseEntity<List<ProductDTO>> getAllProducts() {
        List<ProductDTO> products = productService.getAllProducts();
        return ResponseEntity.ok(products);
    }

    /**
     * Retorna um produto específico pelo ID.
     * Acesso: Público.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getProductById(@PathVariable Long id) {
        ProductDTO product = productService.getProductById(id);
        return ResponseEntity.ok(product);
    }

    /**
     * Cria um novo produto.
     * Acesso: Apenas ADMIN.
     */
    @PostMapping(consumes = "multipart/form-data")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CreateResponseDTO> createProduct(
            @Valid @ModelAttribute CreateProductDTO productDTO,
            @RequestParam(value = "image", required = false) MultipartFile imageFile) {

        CreateResponseDTO response = productService.createProduct(productDTO, imageFile);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Atualiza um produto existente.
     * Acesso: Apenas ADMIN.
     */
    @PutMapping(value = "/{id}", consumes = "multipart/form-data")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UpdateResponseDTO> updateProduct(
            @PathVariable Long id,
            @Valid UpdateProductDTO productUpdateDTO,
            @RequestParam(value = "imageFile", required = false) MultipartFile imageFile) {

        UpdateResponseDTO updatedProduct = productService.updateProduct(id, productUpdateDTO, imageFile);
        return ResponseEntity.ok(updatedProduct);
    }

    /**
     * Deleta um produto pelo ID.
     * Acesso: Apenas ADMIN.
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CreateResponseDTO> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.ok(new CreateResponseDTO(id.toString(), "Produto deletado com sucesso!"));
    }
}