package com.kiora.micromarket.entity;

import jakarta.persistence.*; // Para @Entity, @Table, @Id, @GeneratedValue, @Column, @ManyToMany
import lombok.*;               // Para @Data, @NoArgsConstructor, @AllArgsConstructor
import java.util.List;         // Para poder usar List<>

@Entity
@Table(name = "providers")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Provider {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true) // Regla de Negocio 2
    private String taxId; // El NIT

    @Column(nullable = false)
    private String name;

    private String phone;

    @ManyToMany(mappedBy = "providers") // La relación ManyToMany (Inversa)
    private List<Product> products;
}