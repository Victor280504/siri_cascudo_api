package com.progweb.siri_cascudo_api.dto.Sale;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class UpdateSaleDTO {
    private Date date;

    private String paymentMethod;

    private Long idUser;

    private List<SaleProductDTO> products;
}
