package com.kiora.micromarket.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CategoryRequestDTO {
    @NotBlank(message = "El nombre es obligatorio")
    private String name;
    
    private String description;
}