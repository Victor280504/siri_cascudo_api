package com.progweb.siri_cascudo_api.dto.Sale;

import com.progweb.siri_cascudo_api.model.Product;
import lombok.Data;

@Data
public class SaleProductWithProductDTO {
    private Product product;
    private Integer quantity;
    private double value;
}
