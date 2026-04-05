package com.kiora.micromarket.controller;

import com.kiora.micromarket.dto.request.SaleRequestDTO;
import com.kiora.micromarket.dto.response.SaleResponseDTO;
import com.kiora.micromarket.service.SaleService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/sales")
public class SaleController {

    private final SaleService saleService;

    public SaleController(SaleService saleService) {
        this.saleService = saleService;
    }

    @PostMapping
    public ResponseEntity<SaleResponseDTO> createSale(@Valid @RequestBody SaleRequestDTO request) {
        SaleResponseDTO response = saleService.createSale(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
