package com.kiora.micromarket.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProviderResponseDTO {
    private Long id;
    private String taxId;
    private String name;
    private String phone;

}