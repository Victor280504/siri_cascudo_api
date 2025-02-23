package com.progweb.siri_cascudo_api.dto.Sale;

import lombok.Data;

@Data
public class SaleProductDTO {
    private Long idProduct;
    private Integer quantity;
    private double value;
}
