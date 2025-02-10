package com.progweb.siri_cascudo_api.dto.Product;

import lombok.Data;

@Data
public class ProductDTO {
    private Long id;
    private String name;
    private String description;
    private String image;
    private int quantity;
    private double price;
    private Long idCategory;
}
