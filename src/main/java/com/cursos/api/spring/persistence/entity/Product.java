package com.cursos.api.spring.persistence.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Entity
public class Product {

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    private Long id;

    private String name;

    private BigDecimal price;

    @Enumerated( EnumType.STRING )
    private ProductStatus status;

    @ManyToOne
    @JoinColumn( name = "category_id" )
    private Category category;

    public static enum ProductStatus {
        ENABLED, DISABLED
    }

}