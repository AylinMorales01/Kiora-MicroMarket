package com.kiora.micromarket.controller;

import com.kiora.micromarket.dto.request.SaleRequestDTO;
import com.kiora.micromarket.dto.response.MessageResponseDTO; // ¡Importante!
import com.kiora.micromarket.dto.response.SaleResponseDTO;
import com.kiora.micromarket.service.SaleService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/sales")
@RequiredArgsConstructor
public class SaleController {

    private final SaleService saleService;

    @PostMapping
    public ResponseEntity<?> createSale(@Valid @RequestBody SaleRequestDTO request) {
        try {
            SaleResponseDTO response = saleService.createSale(request);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            MessageResponseDTO errorResponse = new MessageResponseDTO();
            errorResponse.setMessage(e.getMessage());
            // Retorna 400 Bad Request si falla el stock o falta un dato
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllSales() {
        try {
            List<SaleResponseDTO> response = saleService.getAllSales();
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            MessageResponseDTO errorResponse = new MessageResponseDTO();
            errorResponse.setMessage("Error al obtener el listado de ventas");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getSaleById(@PathVariable Long id) {
        try {
            SaleResponseDTO response = saleService.getSaleById(id);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            MessageResponseDTO errorResponse = new MessageResponseDTO();
            errorResponse.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> cancelSale(@PathVariable Long id) {
        try {
            saleService.cancelSale(id);
            MessageResponseDTO successResponse = new MessageResponseDTO();
            successResponse.setMessage("Venta anulada exitosamente. El stock ha sido devuelto.");
            return ResponseEntity.ok(successResponse);
        } catch (RuntimeException e) {
            MessageResponseDTO errorResponse = new MessageResponseDTO();
            errorResponse.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }
}