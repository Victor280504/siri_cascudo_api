package com.progweb.siri_cascudo_api.dto.Sale;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class SaleDetailsDTO {
    private Date date;

    private String paymentMethod;

    private Long idUser;

    private Double total;

    private List<SaleProductWithProductDTO> products;
}
