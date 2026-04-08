package com.kiora.micromarket.dto.response;

import lombok.Data;

@Data
public class SaleDetailResponseDTO {
    private Long productId;
    private String productName;
    private int quantity;
    private double unitPrice;
    private double subtotal;
}
