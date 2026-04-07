package com.kiora.micromarket.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ProviderRequestDTO {
    
    @NotBlank(message = "El NIT es obligatorio y no puede estar vacío")
    private String taxId;
    
    @NotBlank(message = "El nombre del proveedor es obligatorio")
    private String name;
    
    private String phone; // El teléfono puede ser opcional, por eso no lleva @NotBlank
}