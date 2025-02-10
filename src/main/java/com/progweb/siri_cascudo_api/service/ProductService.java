package com.progweb.siri_cascudo_api.service;

import com.progweb.siri_cascudo_api.dto.CreateResponseDTO;
import com.progweb.siri_cascudo_api.dto.Product.CreateProductDTO;
import com.progweb.siri_cascudo_api.dto.UpdateResponseDTO;
import com.progweb.siri_cascudo_api.dto.Product.ProductDTO;
import com.progweb.siri_cascudo_api.dto.Product.UpdateProductDTO;
import com.progweb.siri_cascudo_api.exception.ResourceNotFoundException;
import com.progweb.siri_cascudo_api.model.Product;
import com.progweb.siri_cascudo_api.repository.ProductRepository;
import com.progweb.siri_cascudo_api.util.LocalStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private LocalStorageService localStorageService;

    public ProductDTO getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Produto", "id", id));
        return mapToProductDTO(product);
    }

    public List<ProductDTO> getAllProducts() {
        return productRepository.findAll()
                .stream()
                .map(this::mapToProductDTO)
                .collect(Collectors.toList());
    }

    public CreateResponseDTO createProduct(CreateProductDTO productDTO, MultipartFile imageFile) {
        Product product = new Product();
        product.setName(productDTO.getName());
        product.setDescription(productDTO.getDescription());
        product.setPrice(Objects.nonNull(productDTO.getPrice()) ? productDTO.getPrice() : 0.0);
        product.setQuantity(Objects.nonNull(productDTO.getQuantity()) ? productDTO.getQuantity() : 0);
        product.setIdCategory(productDTO.getIdCategory());

        if (imageFile != null && !imageFile.isEmpty()) {
            String imageName = localStorageService.saveImage(imageFile);
            product.setImage(imageName);
        }

        Product savedProduct = productRepository.save(product);
        return new CreateResponseDTO(savedProduct.getId().toString(), "Produto criado com sucesso.");
    }

    public UpdateResponseDTO updateProduct(Long id, UpdateProductDTO productDTO, MultipartFile imageFile) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Produto", "id", id));

        if (productDTO.getName() != null && !productDTO.getName().isEmpty()) {
            product.setName(productDTO.getName());
        }
        if (productDTO.getDescription() != null && !productDTO.getDescription().isEmpty()) {
            product.setDescription(productDTO.getDescription());
        }
        if (productDTO.getPrice() != product.getPrice()) {
            product.setPrice(productDTO.getPrice());
        }
        if (productDTO.getQuantity() != product.getQuantity()) {
            product.setQuantity(productDTO.getQuantity());
        }

        if (imageFile != null && !imageFile.isEmpty()) {
            String imageName = localStorageService.saveImage(imageFile);
            product.setImage(imageName);
        }

        Product updatedProduct = productRepository.save(product);
        return new UpdateResponseDTO(updatedProduct.getId().toString(), "Produto atualizado com sucesso.",
                mapToProductDTO(updatedProduct));
    }

    public CreateResponseDTO deleteProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Produto", "id", id));

        if (product.getImage() != null && !product.getImage().isEmpty()) {
            localStorageService.deleteImage(product.getImage());
        }

        productRepository.delete(product);
        return new CreateResponseDTO(id.toString(), "Produto deletado com sucesso.");
    }

    private ProductDTO mapToProductDTO(Product product) {
        ProductDTO productDTO = new ProductDTO();
        productDTO.setId(product.getId());
        productDTO.setName(product.getName());
        productDTO.setDescription(product.getDescription());
        productDTO.setQuantity(product.getQuantity());
        productDTO.setPrice(product.getPrice());
        productDTO.setIdCategory(product.getIdCategory());
        productDTO.setImage(product.getImage());
        return productDTO;
    }
}