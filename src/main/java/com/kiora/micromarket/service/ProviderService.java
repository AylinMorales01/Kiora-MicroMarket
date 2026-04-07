package com.kiora.micromarket.service;

import com.kiora.micromarket.dto.request.ProviderRequestDTO;
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


    public List<ProviderResponseDTO> getAllProviders() {
        return providerRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    // Crear proveedor
    public ProviderResponseDTO createProvider(ProviderRequestDTO requestDTO) {
        // La validación de NIT vacío ya no es necesaria aquí porque el @NotBlank del DTO lo frena antes
        
        if (providerRepository.existsByTaxId(requestDTO.getTaxId())) {
            throw new RuntimeException("El NIT ya se encuentra registrado en la base de datos.");
        }

        // Convertimos el DTO a la Entidad
        Provider provider = new Provider();
        provider.setTaxId(requestDTO.getTaxId());
        provider.setName(requestDTO.getName());
        provider.setPhone(requestDTO.getPhone());

        Provider savedProvider = providerRepository.save(provider);
        return mapToDTO(savedProvider);
    }

    // Actualizar proveedor
    public ProviderResponseDTO updateProvider(Long id, ProviderRequestDTO requestDTO) {
        Provider existingProvider = providerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Proveedor no encontrado con ID: " + id));

        if (requestDTO.getTaxId() != null && !requestDTO.getTaxId().isBlank()) {
            if (!existingProvider.getTaxId().equals(requestDTO.getTaxId()) &&
                    providerRepository.existsByTaxId(requestDTO.getTaxId())) {
                throw new RuntimeException("El NIT ya se encuentra registrado en la base de datos.");
            }
            existingProvider.setTaxId(requestDTO.getTaxId());
        }

        if (requestDTO.getName() != null) {
            existingProvider.setName(requestDTO.getName());
        }
        if (requestDTO.getPhone() != null) {
            existingProvider.setPhone(requestDTO.getPhone());
        }

        Provider updatedProvider = providerRepository.save(existingProvider);
        return mapToDTO(updatedProvider);
    }

    // Eliminar proveedor
    public void deleteProvider(Long id) {
        Provider provider = providerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Proveedor no encontrado con ID: " + id));

        // Esto es para evitar que se elimine un proveedor que ya tiene productos asociados
        if (provider.getProducts() != null && !provider.getProducts().isEmpty()) {
            throw new RuntimeException("No se puede eliminar el proveedor porque tiene productos asociados en el inventario.");
        }

        providerRepository.delete(provider);
    }

    // Entrada de almacen

    @Transactional
    public void registerWarehouseInput(WarehouseInputRequestDTO inputDTO) {
        // Valida si existe el proveedor
        providerRepository.findById(inputDTO.getProviderId())
                .orElseThrow(() -> new RuntimeException("Proveedor no encontrado con ID: " + inputDTO.getProviderId()));

        // Valida si exisste el producto
        Product product = productRepository.findById(inputDTO.getProductId())
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con ID: " + inputDTO.getProductId()));

        // Actualiza el stock del producto sumando la cantidad de la entrada
        int newStock = product.getStock() + inputDTO.getQuantity();
        product.setStock(newStock);

        // Gurda el nuevo stock del producto en db
        productRepository.save(product);
    }

    // Mapeo de entidad a DTO (esto se hace para no exponer la entidad directamente y controlar qué campos se muestran en la respuesta)

    private ProviderResponseDTO mapToDTO(Provider provider) {
        return ProviderResponseDTO.builder()
                .id(provider.getId())
                .taxId(provider.getTaxId())
                .name(provider.getName())
                .phone(provider.getPhone())
                .build();
    }
}