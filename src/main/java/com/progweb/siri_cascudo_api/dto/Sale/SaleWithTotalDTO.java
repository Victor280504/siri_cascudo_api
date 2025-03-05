package com.progweb.siri_cascudo_api.dto.Sale;

import lombok.Data;

import java.util.Date;

@Data
public class SaleWithTotalDTO {
    private Long id;

    private Date date;

    private String paymentMethod;

    private Long idUser;

    private double total;

}
