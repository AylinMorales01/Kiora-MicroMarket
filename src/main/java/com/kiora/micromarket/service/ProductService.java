package com.kiora.micromarket.service;

import com.kiora.micromarket.dto.request.ProductRequestDTO;
import com.kiora.micromarket.dto.response.ProductResponseDTO;
import com.kiora.micromarket.entity.Category;
import com.kiora.micromarket.entity.Product;
import com.kiora.micromarket.repository.CategoryRepository;
import com.kiora.micromarket.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    // Crear producto
    public ProductResponseDTO save(ProductRequestDTO requestDTO) {
        if (productRepository.findByBarcode(requestDTO.getBarcode()).isPresent()) {
            throw new RuntimeException("El código de barras ya existe en el sistema.");
        }

        // Se busca la categoría por su ID, para verificar que exista y luego asignarla al producto
        Category category = categoryRepository.findById(requestDTO.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada con ID: " + requestDTO.getCategoryId()));

        Product product = new Product();
        product.setName(requestDTO.getName());
        product.setDescription(requestDTO.getDescription());
        product.setBarcode(requestDTO.getBarcode());
        product.setPrice(requestDTO.getPrice());
        product.setStock(requestDTO.getStock());
        product.setCategory(category);
        product.setActive(true);

        Product savedProduct = productRepository.save(product);
        return mapToDTO(savedProduct);
    }


    // Listar productos
    public List<ProductResponseDTO> findAll() {
        return productRepository.findAll().stream()
                .filter(Product::isActive)
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    // Traer producto por ID
    public ProductResponseDTO findById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
        
        if (!product.isActive()) {
            throw new RuntimeException("El producto se encuentra inactivo (eliminado lógicamente).");
        }
        
        return mapToDTO(product);
    }

    // Eliminación lógica
    public void softDelete(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
        product.setActive(false);
        productRepository.save(product);
    }

    
    private ProductResponseDTO mapToDTO(Product product) {
        return ProductResponseDTO.builder()
                .id(product.getId())
                .name(product.getName())
                .barcode(product.getBarcode())
                .price(product.getPrice())
                .stock(product.getStock())
                .active(product.isActive())
                .build();
    }
}