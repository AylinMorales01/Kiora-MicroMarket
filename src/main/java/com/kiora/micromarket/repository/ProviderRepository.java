package com.kiora.micromarket.repository;

import com.kiora.micromarket.entity.Provider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProviderRepository extends JpaRepository<Provider, Long> {
    boolean existsByTaxId(String taxId);
}