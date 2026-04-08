package com.kiora.micromarket.dto.response;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class SaleResponseDTO {
    private Long id;
    private LocalDateTime date;
    private double subtotal;
    private double iva;
    private double total;
    private String employeeName;
    private boolean active;
    private List<SaleDetailResponseDTO> details;
}
