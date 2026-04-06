package com.kiora.micromarket.service;

import com.kiora.micromarket.entity.Category;
import com.kiora.micromarket.entity.Product;
import com.kiora.micromarket.repository.CategoryRepository;
import com.kiora.micromarket.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public ProductService(ProductRepository productRepository,
                          CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    public Product save(Product product) {

        // 🔥 VALIDAR BARCODE
        productRepository.findByBarcode(product.getBarcode())
                .ifPresent(p -> {
                    throw new RuntimeException("Barcode already exists");
                });

        // 🔥 VALIDAR CATEGORY
        if (product.getCategory() != null) {
            Long categoryId = product.getCategory().getId();

            Category category = categoryRepository.findById(categoryId)
                    .orElseThrow(() -> new RuntimeException("Category not found"));

            product.setCategory(category);
        }

        return productRepository.save(product);
    }

    public List<Product> findAll() {
        return productRepository.findAll()
                .stream()
                .filter(Product::isActive)
                .toList();
    }

    public Product findById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
    }

    public void softDelete(Long id) {
        Product product = findById(id);
        product.setActive(false);
        productRepository.save(product);
    }
}
