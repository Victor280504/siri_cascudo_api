package com.progweb.siri_cascudo_api.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "recipe")
public class Recipe {

    @EmbeddedId
    private RecipeId id;

    @Column(nullable = false)
    private Integer quantity;
}
