package com.kiora.micromarket.service;

import com.kiora.micromarket.dto.request.WarehouseInputRequestDTO;
import com.kiora.micromarket.entity.Provider;
import com.kiora.micromarket.entity.Product;
import com.kiora.micromarket.repository.ProviderRepository;
import com.kiora.micromarket.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
@Service
@RequiredArgsConstructor
public class ProviderService { 

    private final ProviderRepository providerRepository;
    private final ProductRepository productRepository;

    @Transactional
    public void registerWarehouseInput(WarehouseInputRequestDTO inputDTO) {
        // 1. Validar que el proveedor existe
        Provider provider = providerRepository.findById(inputDTO.getProviderId())
                .orElseThrow(() -> new RuntimeException("Provider not found with ID: " + inputDTO.getProviderId()));

        // 2. Validar que el producto existe
        Product product = productRepository.findById(inputDTO.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found with ID: " + inputDTO.getProductId()));

        // 3. Regla de Negocio 1: Sumar unidades al stock actual
        // (Asegúrate que el campo en Product se llame 'stock')
        int updatedStock = product.getStock() + inputDTO.getQuantity();
        product.setStock(updatedStock);

        // 4. Guardar cambios
        productRepository.save(product);
    }
}