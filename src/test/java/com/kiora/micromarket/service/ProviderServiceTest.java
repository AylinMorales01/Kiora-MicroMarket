package com.kiora.micromarket.service;

import com.kiora.micromarket.dto.request.ProviderRequestDTO;
import com.kiora.micromarket.dto.request.WarehouseInputRequestDTO;
import com.kiora.micromarket.dto.response.ProviderResponseDTO;
import com.kiora.micromarket.entity.Product;
import com.kiora.micromarket.entity.Provider;
import com.kiora.micromarket.repository.ProductRepository;
import com.kiora.micromarket.repository.ProviderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProviderServiceTest {

    @Mock
    private ProviderRepository providerRepository;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProviderService providerService;

    private Provider provider;
    private ProviderRequestDTO providerRequestDTO;

    @BeforeEach
    void setUp() {
        provider = new Provider();
        provider.setId(1L);
        provider.setTaxId("123456789");
        provider.setName("Test Provider");
        provider.setPhone("12345678");
        provider.setProducts(Collections.emptyList());

        providerRequestDTO = new ProviderRequestDTO();
        providerRequestDTO.setTaxId("123456789");
        providerRequestDTO.setName("Test Provider");
        providerRequestDTO.setPhone("12345678");
    }

    @Test
    void testGetAllProviders() {
        when(providerRepository.findAll()).thenReturn(List.of(provider));

        List<ProviderResponseDTO> response = providerService.getAllProviders();

        assertNotNull(response);
        assertEquals(1, response.size());
        assertEquals("123456789", response.get(0).getTaxId());
        verify(providerRepository, times(1)).findAll();
    }

    @Test
    void testCreateProvider_Success() {
        when(providerRepository.existsByTaxId(providerRequestDTO.getTaxId())).thenReturn(false);
        when(providerRepository.save(any(Provider.class))).thenReturn(provider);

        ProviderResponseDTO response = providerService.createProvider(providerRequestDTO);

        assertNotNull(response);
        assertEquals("123456789", response.getTaxId());
        assertEquals("Test Provider", response.getName());
        verify(providerRepository, times(1)).existsByTaxId(anyString());
        verify(providerRepository, times(1)).save(any(Provider.class));
    }

    @Test
    void testCreateProvider_TaxIdAlreadyExists() {
        when(providerRepository.existsByTaxId(providerRequestDTO.getTaxId())).thenReturn(true);

        Exception exception = assertThrows(RuntimeException.class, () -> {
            providerService.createProvider(providerRequestDTO);
        });

        assertEquals("El NIT ya se encuentra registrado en la base de datos.", exception.getMessage());
        verify(providerRepository, never()).save(any(Provider.class));
    }

    @Test
    void testUpdateProvider_Success() {
        when(providerRepository.findById(1L)).thenReturn(Optional.of(provider));
        when(providerRepository.save(any(Provider.class))).thenReturn(provider);

        providerRequestDTO.setName("Updated Provider");
        ProviderResponseDTO response = providerService.updateProvider(1L, providerRequestDTO);

        assertNotNull(response);
        verify(providerRepository, times(1)).save(any(Provider.class));
    }

    @Test
    void testUpdateProvider_TaxIdAlreadyExists() {
        providerRequestDTO.setTaxId("987654321");
        
        when(providerRepository.findById(1L)).thenReturn(Optional.of(provider));
        when(providerRepository.existsByTaxId(providerRequestDTO.getTaxId())).thenReturn(true);

        Exception exception = assertThrows(RuntimeException.class, () -> {
            providerService.updateProvider(1L, providerRequestDTO);
        });

        assertEquals("El NIT ya se encuentra registrado en la base de datos.", exception.getMessage());
        verify(providerRepository, never()).save(any(Provider.class));
    }

    @Test
    void testUpdateProvider_NotFound() {
        when(providerRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            providerService.updateProvider(1L, providerRequestDTO);
        });

        assertEquals("Proveedor no encontrado con ID: 1", exception.getMessage());
        verify(providerRepository, never()).save(any(Provider.class));
    }

    @Test
    void testDeleteProvider_Success() {
        when(providerRepository.findById(1L)).thenReturn(Optional.of(provider));

        providerService.deleteProvider(1L);

        verify(providerRepository, times(1)).delete(provider);
    }

    @Test
    void testDeleteProvider_HasProducts() {
        Product product = new Product();
        product.setId(1L);
        provider.setProducts(List.of(product));

        when(providerRepository.findById(1L)).thenReturn(Optional.of(provider));

        Exception exception = assertThrows(RuntimeException.class, () -> {
            providerService.deleteProvider(1L);
        });

        assertEquals("No se puede eliminar el proveedor porque tiene productos asociados en el inventario.", exception.getMessage());
        verify(providerRepository, never()).delete(any(Provider.class));
    }

    @Test
    void testDeleteProvider_NotFound() {
        when(providerRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            providerService.deleteProvider(1L);
        });

        assertEquals("Proveedor no encontrado con ID: 1", exception.getMessage());
        verify(providerRepository, never()).delete(any(Provider.class));
    }

    @Test
    void testRegisterWarehouseInput_Success() {
        WarehouseInputRequestDTO inputDTO = new WarehouseInputRequestDTO();
        inputDTO.setProviderId(1L);
        inputDTO.setProductId(1L);
        inputDTO.setQuantity(10);

        Product product = new Product();
        product.setId(1L);
        product.setStock(5);

        when(providerRepository.findById(1L)).thenReturn(Optional.of(provider));
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(productRepository.save(any(Product.class))).thenReturn(product);

        providerService.registerWarehouseInput(inputDTO);

        assertEquals(15, product.getStock());
        verify(productRepository, times(1)).save(product);
    }

    @Test
    void testRegisterWarehouseInput_ProviderNotFound() {
        WarehouseInputRequestDTO inputDTO = new WarehouseInputRequestDTO();
        inputDTO.setProviderId(1L);

        when(providerRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            providerService.registerWarehouseInput(inputDTO);
        });

        assertEquals("Proveedor no encontrado con ID: 1", exception.getMessage());
        verify(productRepository, never()).findById(anyLong());
        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    void testRegisterWarehouseInput_ProductNotFound() {
        WarehouseInputRequestDTO inputDTO = new WarehouseInputRequestDTO();
        inputDTO.setProviderId(1L);
        inputDTO.setProductId(1L);

        when(providerRepository.findById(1L)).thenReturn(Optional.of(provider));
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            providerService.registerWarehouseInput(inputDTO);
        });

        assertEquals("Producto no encontrado con ID: 1", exception.getMessage());
        verify(productRepository, never()).save(any(Product.class));
    }
}
