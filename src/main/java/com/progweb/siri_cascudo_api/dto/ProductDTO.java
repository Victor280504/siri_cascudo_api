package com.progweb.siri_cascudo_api.dto;

import com.progweb.siri_cascudo_api.model.Product;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class ProductDTO {
    private Long id;
    @NotBlank(message = "O nome do produto é obrigatório.")
    @Size(min = 3, max = 100, message = "O nome deve ter entre 3 e 100 caracteres.")
    private String name;

    @Size(max = 500, message = "A descrição pode ter no máximo 500 caracteres.")
    private String description;

    private String image;

    @Min(value = 0, message = "A quantidade não pode ser negativa.")
    private int quantity;

    @DecimalMin(value = "0.01", message = "O preço deve ser maior que zero.")
    private double price;

    @NotNull(message = "A categoria do produto é obrigatória.")
    private Long idCategory;

    public ProductDTO() {
    }

    public ProductDTO(Long id, String name, String description, String image, int quantity, double price,
            Long idCategory) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.image = image;
        this.quantity = quantity;
        this.price = price;
        this.idCategory = idCategory;
    }

    public ProductDTO(Product product) {
        this.id = product.getId();
        this.name = product.getName();
        this.description = product.getDescription();
        this.image = product.getImage();
        this.quantity = product.getQuantity();
        this.price = product.getPrice();
        this.idCategory = product.getIdCategory();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Long getIdCategory() {
        return idCategory;
    }

    public void setIdCategory(Long idCategory) {
        this.idCategory = idCategory;
    }
}
