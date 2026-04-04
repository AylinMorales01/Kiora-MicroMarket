package com.kiora.micromarket.service;

import com.kiora.micromarket.entity.Product;
import com.kiora.micromarket.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Product save(Product product) {
        productRepository.findByBarcode(product.getBarcode())
                .ifPresent(p -> {
                    throw new RuntimeException("Barcode already exists");
                });

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