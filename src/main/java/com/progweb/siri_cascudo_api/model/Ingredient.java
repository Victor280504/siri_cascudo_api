package com.progweb.siri_cascudo_api.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "ingredient")
@Data
public class Ingredient
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Integer quantity;
    private String description;
}