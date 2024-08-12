package com.cursos.api.spring.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class SaveProduct {

    @NotBlank( message = "El nombre del producto no puede ir vacío" )
    private String name;

    @DecimalMin( value = "0.01", message = "El precio debe ser mayor a 0" )
    private BigDecimal price;

    @Min( value = 1 )
    private Long categoryId;

}