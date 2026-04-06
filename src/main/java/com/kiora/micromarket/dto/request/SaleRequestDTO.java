package com.kiora.micromarket.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.Valid;
import java.util.List;

public class SaleRequestDTO {

    @NotNull(message = "El ID del empleado es obligatorio")
    private Long employeeId;

    @NotEmpty(message = "La venta debe contener al menos un producto")
    @Valid
    private List<SaleDetailRequestDTO> details;

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    public List<SaleDetailRequestDTO> getDetails() {
        return details;
    }

    public void setDetails(List<SaleDetailRequestDTO> details) {
        this.details = details;
    }
}
