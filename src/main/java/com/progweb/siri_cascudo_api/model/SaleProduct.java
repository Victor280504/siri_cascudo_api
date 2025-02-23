package com.progweb.siri_cascudo_api.model;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "sale_product")
public class SaleProduct {
    @EmbeddedId
    private SaleProductId id;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false)
    private double value;
}
