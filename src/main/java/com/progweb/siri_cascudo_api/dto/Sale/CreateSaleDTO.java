package com.progweb.siri_cascudo_api.dto.Sale;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class CreateSaleDTO {
    private Date date;

    @NotBlank(message = "O método de pagamento é obrigatório.")
    private String paymentMethod;

    private Long idUser;

    private List<SaleProductDTO> products;

}
