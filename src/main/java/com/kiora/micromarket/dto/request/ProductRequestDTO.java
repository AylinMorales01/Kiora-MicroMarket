package com.kiora.micromarket.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ProductRequestDTO {
    @NotBlank(message = "El nombre del producto es obligatorio")
    private String name;
    
    private String description;
    
    @NotBlank(message = "El código de barras es obligatorio")
    private String barcode;
    
    @Min(value = 0, message = "El precio del producto no puede ser negativo")
    private double price;
    
    @Min(value = 0, message = "El stock del producto no puede ser negativo")
    private int stock;
    
    @NotNull(message = "Se requiere el ID de una categoría para el crear el producto")
    private Long categoryId;
}