package com.kiora.micromarket.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductResponseDTO {
    private Long id;
    private String name;
    private String barcode;
    private double price;
    private int stock;
    private boolean active;
}