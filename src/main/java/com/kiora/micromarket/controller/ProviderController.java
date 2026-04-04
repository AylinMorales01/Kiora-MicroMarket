package com.kiora.micromarket.controller;

import com.kiora.micromarket.dto.request.WarehouseInputRequestDTO;
import com.kiora.micromarket.service.ProviderService; // Importamos la clase directa
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/providers")
@RequiredArgsConstructor
public class ProviderController {

    private final ProviderService providerService; // Inyección directa

    @PostMapping("/warehouse-input")
    public ResponseEntity<String> warehouseInput(@Valid @RequestBody WarehouseInputRequestDTO requestDTO) {
        providerService.registerWarehouseInput(requestDTO);
        return ResponseEntity.ok("Stock updated successfully.");
    }
}