package com.kiora.micromarket.dto.response;

import java.util.List;

public class CategoryResponseDto {
    public Long id;
    public String name;
    public String description;
    public List<ProductResponseDTO> products;
}