package com.kiora.micromarket.service;

import com.kiora.micromarket.dto.request.WarehouseInputRequestDTO;
import com.kiora.micromarket.dto.response.ProviderResponseDTO;
import com.kiora.micromarket.entity.Product;
import com.kiora.micromarket.entity.Provider;
import com.kiora.micromarket.repository.ProductRepository;
import com.kiora.micromarket.repository.ProviderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProviderService {

    private final ProviderRepository providerRepository;
    private final ProductRepository productRepository;

    // --- MÉTODOS DE CONSULTA Y CREACIÓN ---

    public List<ProviderResponseDTO> getAllProviders() {
        return providerRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    // Regla de Negocio 2: NIT obligatorio y no repetido
    public ProviderResponseDTO createProvider(Provider provider) {
        if (provider.getTaxId() == null || provider.getTaxId().isBlank()) {
            throw new RuntimeException("El NIT del proveedor es un campo obligatorio.");
        }
        
        if (providerRepository.existsByTaxId(provider.getTaxId())) {
            throw new RuntimeException("El NIT ya se encuentra registrado en la base de datos.");
        }

        Provider savedProvider = providerRepository.save(provider);
        return mapToDTO(savedProvider);
    }

    // --- REGLA DE NEGOCIO 1: ENTRADA DE ALMACÉN ---

    @Transactional
    public void registerWarehouseInput(WarehouseInputRequestDTO inputDTO) {
        // 1. Validar existencia del proveedor
        providerRepository.findById(inputDTO.getProviderId())
                .orElseThrow(() -> new RuntimeException("Proveedor no encontrado con ID: " + inputDTO.getProviderId()));

        // 2. Validar existencia del producto
        Product product = productRepository.findById(inputDTO.getProductId())
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con ID: " + inputDTO.getProductId()));

        // 3. Lógica de negocio: Sumar unidades al stock actual
        int newStock = product.getStock() + inputDTO.getQuantity();
        product.setStock(newStock);

        // 4. Persistir el cambio en el Módulo I
        productRepository.save(product);
    }

    // --- MAPEO ---

    private ProviderResponseDTO mapToDTO(Provider provider) {
        return ProviderResponseDTO.builder()
                .id(provider.getId())
                .taxId(provider.getTaxId())
                .name(provider.getName())
                .phone(provider.getPhone())
                .build();
    }
}