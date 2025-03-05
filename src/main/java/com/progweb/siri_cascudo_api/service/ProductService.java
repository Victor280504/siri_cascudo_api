package com.progweb.siri_cascudo_api.service;

import com.progweb.siri_cascudo_api.dto.CreateResponseDTO;
import com.progweb.siri_cascudo_api.dto.Product.CreateProductDTO;
import com.progweb.siri_cascudo_api.dto.UpdateResponseDTO;
import com.progweb.siri_cascudo_api.dto.Product.ProductDTO;
import com.progweb.siri_cascudo_api.dto.Product.UpdateProductDTO;
import com.progweb.siri_cascudo_api.exception.ResourceNotFoundException;
import com.progweb.siri_cascudo_api.model.Product;
import com.progweb.siri_cascudo_api.repository.ProductRepository;
import com.progweb.siri_cascudo_api.util.storage.Storage;
import com.progweb.siri_cascudo_api.util.storage.StorageStrategyRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.File;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private StorageStrategyRegistry storageStrategyRegistry;

    @Autowired
    private RecipeService recipeService;

    private Storage storageService;

    @PostConstruct
    public void init() {
        this.storageService = storageStrategyRegistry.getSaveStrategy();
    }

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
        product.setPrice(productDTO.getPrice());
        product.setIdCategory(productDTO.getIdCategory());

        if (imageFile != null && !imageFile.isEmpty()) {
            String imageName = storageService.saveImage(imageFile);
            product.setImage(imageName);
        }

        Product savedProduct = productRepository.save(product);
        return new CreateResponseDTO(savedProduct.getId().toString(), "Produto criado com sucesso.");
    }

    public UpdateResponseDTO<ProductDTO> updateProduct(Long id, UpdateProductDTO productDTO, MultipartFile imageFile) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Produto", "id", id));

        if (productDTO.getName() != null && !productDTO.getName().isEmpty()) {
            product.setName(productDTO.getName());
        }
        if (productDTO.getDescription() != null && !productDTO.getDescription().isEmpty()) {
            product.setDescription(productDTO.getDescription());
        }

        if (productDTO.getPrice() != 0 && productDTO.getPrice() != product.getPrice()) {
            product.setPrice(productDTO.getPrice());
        }

        if (productDTO.getIdCategory() != null && !Objects.equals(productDTO.getIdCategory(), product.getIdCategory())) {
            product.setIdCategory(productDTO.getIdCategory());
        }

        if (imageFile != null && !imageFile.isEmpty()) {
            String imageName = storageService.saveImage(imageFile);
            boolean res = storageService.updateImage(product.getImage());
            if (res) {
                product.setImage(imageName);
            } else {
                clearDeletedImageFiles();
            }
        }

        Product updatedProduct = productRepository.save(product);

        return new UpdateResponseDTO<>(updatedProduct.getId().toString(), "Produto atualizado com sucesso.",
                mapToProductDTO(updatedProduct));
    }

    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Produto", "id", id));

        if (product.getImage() != null && !product.getImage().isEmpty()) {
            storageService.deleteImage(product.getImage());
        }

        productRepository.delete(product);
    }

    private ProductDTO mapToProductDTO(Product product) {
        ProductDTO productDTO = new ProductDTO();
        productDTO.setId(product.getId());
        productDTO.setName(product.getName());
        productDTO.setDescription(product.getDescription());
        productDTO.setPrice(product.getPrice());
        productDTO.setAvailable(recipeService.getAvailableQuantity(product.getId()));
        productDTO.setIdCategory(product.getIdCategory());
        productDTO.setImage(product.getImage());
        return productDTO;
    }

    public List<String> getAllImageStrings() {

        List<ProductDTO> products = productRepository.findAll()
                .stream()
                .map(this::mapToProductDTO)
                .toList();

        return products.stream().map(ProductDTO::getImage)
                .collect(Collectors.toList());
    }

    public void clearDeletedImageFiles() {
        List<String> images = getAllImageStrings();
        File folder = new File("caminho/da/pasta");

        // Lista todos os arquivos na pasta
        File[] listOfFiles = folder.listFiles();

        // Verifica se a pasta é válida e não está vazia
        if (listOfFiles != null) {
            for (File file : listOfFiles) {
                if (file.isFile() && !images.contains(file.getName())) {
                    // Deleta o arquivo se não estiver na lista
                    if (file.delete()) {
                        System.out.println("Arquivo deletado: " + file.getName());
                    } else {
                        System.out.println("Falha ao deletar arquivo: " + file.getName());
                    }
                }
            }
        } else {
            System.out.println("A pasta não existe ou está vazia.");
        }
    }

}