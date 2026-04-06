package com.kiora.micromarket.controller;

import com.kiora.micromarket.dto.request.WarehouseInputRequestDTO;
import com.kiora.micromarket.dto.response.ProviderResponseDTO;
import com.kiora.micromarket.entity.Provider;
import com.kiora.micromarket.service.ProviderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/providers")
@RequiredArgsConstructor
public class ProviderController {

    private final ProviderService providerService;

    // Listar
    @GetMapping("/get-all")
    public ResponseEntity<List<ProviderResponseDTO>> getAllProviders() {
        try {
            List<ProviderResponseDTO> response = providerService.getAllProviders();
            return ResponseEntity.status(HttpStatus.FOUND).body(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    // Crear proveedor (sin que se repita NIT)
    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody Provider provider) {
        try {
            ProviderResponseDTO response = providerService.createProvider(provider);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // Entrada de inventario
    @PostMapping("/warehouse-input")
    public ResponseEntity<String> warehouseInput(@Valid @RequestBody WarehouseInputRequestDTO requestDTO) {
        try {
            providerService.registerWarehouseInput(requestDTO);
            return ResponseEntity.ok("Stock actualizado exitosamente tras entrada de almacén.");
        } catch (RuntimeException e) {
            // Retorna el mensaje de error si el proveedor o producto no existen
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al procesar la entrada.");
        }
    }
}