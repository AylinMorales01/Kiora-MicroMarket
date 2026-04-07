package com.kiora.micromarket.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class WarehouseInputRequestDTO {

    @NotNull(message = "Se requiere un id de producto para registrar la entrada de inventario")
    private Long productId;

    @NotNull(message = "Se requiere un id de proveedor para registrar la entrada de inventario")
    private Long providerId;

    @NotNull(message = "Se requiere una cantidad del producto para registrar la entrada de inventario")
    @Min(value = 1, message = "La cantidad debe ser igual o mayor a 1")
    private Integer quantity;
}