package com.kiora.micromarket.controller;

import com.kiora.micromarket.dto.request.CategoryRequestDTO;
import com.kiora.micromarket.dto.response.CategoryResponseDTO;
import com.kiora.micromarket.dto.response.MessageResponseDTO;
import com.kiora.micromarket.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService service;

    @PostMapping("/create")
    public ResponseEntity<?> create(@Valid @RequestBody CategoryRequestDTO requestDTO) {
        try {
            CategoryResponseDTO response = service.save(requestDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (RuntimeException e) {
            MessageResponseDTO errorResponse = new MessageResponseDTO(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    @GetMapping("/get-all")
    public ResponseEntity<?> getAll() {
        try {
            List<CategoryResponseDTO> response = service.findAll();
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            MessageResponseDTO errorResponse = new MessageResponseDTO("Error al intentar obtener las categorías");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        try {
            CategoryResponseDTO response = service.findById(id);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (RuntimeException e) {
            MessageResponseDTO errorResponse = new MessageResponseDTO(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        try {
            service.delete(id);
            MessageResponseDTO successResponse = new MessageResponseDTO("Categoría eliminada exitosamente.");
            return ResponseEntity.status(HttpStatus.OK).body(successResponse);
        } catch (RuntimeException e) {
            MessageResponseDTO errorResponse = new MessageResponseDTO(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }
}