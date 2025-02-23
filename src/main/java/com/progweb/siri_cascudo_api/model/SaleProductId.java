package com.progweb.siri_cascudo_api.model;

import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Embeddable
public class SaleProductId implements Serializable {
    private Long idSale;
    private Long idProduct;
}
