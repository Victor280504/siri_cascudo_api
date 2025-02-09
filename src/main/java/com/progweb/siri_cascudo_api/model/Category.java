package com.progweb.siri_cascudo_api.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Table(name = "category")
@Entity(name = "category")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
}
