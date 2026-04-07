package com.kiora.micromarket.service;

import com.kiora.micromarket.dto.request.CategoryRequestDTO;
import com.kiora.micromarket.dto.response.CategoryResponseDTO;
import com.kiora.micromarket.dto.response.ProductResponseDTO;
import com.kiora.micromarket.entity.Category;
import com.kiora.micromarket.entity.Product;
import com.kiora.micromarket.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    // Crear categoria
    public CategoryResponseDTO save(CategoryRequestDTO requestDTO) {
        Category category = new Category();
        category.setName(requestDTO.getName());
        category.setDescription(requestDTO.getDescription());

        Category savedCategory = categoryRepository.save(category);
        return mapToDTO(savedCategory);
    }

    // Listar caterorias
    public List<CategoryResponseDTO> findAll() {
        return categoryRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    // Listar categoria por ID
    public CategoryResponseDTO findById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada con ID: " + id));
        return mapToDTO(category);
    }

    // Eliminar categoria
    public void delete(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada con ID: " + id));

        // Esto hace que no se pueda eliminar una categoría si tiene productos asociados a ella
        if (category.getProducts() != null && !category.getProducts().isEmpty()) {
            throw new RuntimeException("No se puede eliminar la categoría porque ya tiene productos asociados.");
        }

        categoryRepository.delete(category);
    }

    // --- MAPEO ---
    private CategoryResponseDTO mapToDTO(Category category) {
        List<ProductResponseDTO> productDTOs = null;
        
        if (category.getProducts() != null) {
            productDTOs = category.getProducts().stream()
                    .filter(Product::isActive) // Esto hace que solo se muestren productos activos y no los borrados lógicamente
                    .map(product -> ProductResponseDTO.builder()
                            .id(product.getId())
                            .name(product.getName())
                            .barcode(product.getBarcode())
                            .price(product.getPrice())
                            .stock(product.getStock())
                            .active(product.isActive())
                            .build())
                    .collect(Collectors.toList());
        }

        return CategoryResponseDTO.builder()
                .id(category.getId())
                .name(category.getName())
                .description(category.getDescription())
                .products(productDTOs)
                .build();
    }
}