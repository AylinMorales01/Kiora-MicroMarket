package com.kiora.micromarket.controller;

import com.kiora.micromarket.dto.request.ProductRequestDTO;
import com.kiora.micromarket.dto.response.MessageResponseDTO;
import com.kiora.micromarket.dto.response.ProductResponseDTO;
import com.kiora.micromarket.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService service;

    @PostMapping("/create")
    public ResponseEntity<?> create(@Valid @RequestBody ProductRequestDTO requestDTO) {
        try {
            ProductResponseDTO response = service.save(requestDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (RuntimeException e) {
            MessageResponseDTO errorResponse = new MessageResponseDTO(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    @GetMapping("/get-all")
    public ResponseEntity<?> getAll() {
        try {
            List<ProductResponseDTO> response = service.findAll();
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            MessageResponseDTO errorResponse = new MessageResponseDTO("Error al traer los productos.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        try {
            ProductResponseDTO response = service.findById(id);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (RuntimeException e) {
            MessageResponseDTO errorResponse = new MessageResponseDTO(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        try {
            service.softDelete(id);
            MessageResponseDTO successResponse = new MessageResponseDTO("Producto eliminado lógicamente.");
            return ResponseEntity.status(HttpStatus.OK).body(successResponse);
        } catch (RuntimeException e) {
            MessageResponseDTO errorResponse = new MessageResponseDTO(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
    }
}