package com.kiora.micromarket.controller;

import com.kiora.micromarket.dto.request.ProviderRequestDTO;
import com.kiora.micromarket.dto.request.WarehouseInputRequestDTO;
import com.kiora.micromarket.dto.response.MessageResponseDTO;
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
    public ResponseEntity<?> getAllProviders() {
        try {
            List<ProviderResponseDTO> response = providerService.getAllProviders();
            return ResponseEntity.status(HttpStatus.FOUND).body(response);
        } catch (RuntimeException e) {
            MessageResponseDTO errorResponse = new MessageResponseDTO(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    // Crear proveedor
    @PostMapping("/create")
    public ResponseEntity<?> create(@Valid @RequestBody ProviderRequestDTO requestDTO) {
        try {
            ProviderResponseDTO response = providerService.createProvider(requestDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (RuntimeException e) {
            MessageResponseDTO errorResponse = new MessageResponseDTO(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    // Actualizar proveedor
    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @Valid @RequestBody ProviderRequestDTO requestDTO) {
        // ... (El bloque try-catch queda exactamente igual que antes)
        try {
            ProviderResponseDTO response = providerService.updateProvider(id, requestDTO);
            MessageResponseDTO successResponse = new MessageResponseDTO("Proveedor actualizado exitosamente.");
            return ResponseEntity.ok(successResponse);
        } catch (RuntimeException e) {
            MessageResponseDTO errorResponse = new MessageResponseDTO(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        } catch (Exception e) {
            MessageResponseDTO errorResponse = new MessageResponseDTO("Error interno al actualizar el proveedor.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    // Eliminar proveedor
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        try {
            providerService.deleteProvider(id);
            MessageResponseDTO successResponse = new MessageResponseDTO("Proveedor eliminado exitosamente.");
            return ResponseEntity.ok(successResponse);
        } catch (RuntimeException e) {
            // Retorna el mensaje de error si el proveedor no existe
            MessageResponseDTO errorResponse = new MessageResponseDTO(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
    }

    // Entrada de inventario
    @PostMapping("/warehouse-input")
    public ResponseEntity<?> warehouseInput(@Valid @RequestBody WarehouseInputRequestDTO requestDTO) {
        try {
            providerService.registerWarehouseInput(requestDTO);
            // También devolvemos el éxito usando la misma estructura JSON
            MessageResponseDTO successResponse = new MessageResponseDTO("Stock actualizado exitosamente tras entrada de almacén.");
            return ResponseEntity.ok(successResponse);
        } catch (RuntimeException e) {
            // Retorna el mensaje de error si el proveedor o producto no existen
            MessageResponseDTO errorResponse = new MessageResponseDTO(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        } catch (Exception e) {
            MessageResponseDTO errorResponse = new MessageResponseDTO("Error al procesar la entrada.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
}