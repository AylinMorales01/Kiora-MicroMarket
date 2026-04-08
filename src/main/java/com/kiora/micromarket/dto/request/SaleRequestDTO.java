package com.kiora.micromarket.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.Valid;
import lombok.Data;

import java.util.List;

@Data
public class SaleRequestDTO {

    @NotNull(message = "El ID del empleado es obligatorio")
    private Long employeeId;

    @NotEmpty(message = "La venta debe contener al menos un producto")
    @Valid
    private List<SaleDetailRequestDTO> details;
}
