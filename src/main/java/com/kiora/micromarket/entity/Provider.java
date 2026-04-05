package com.kiora.micromarket.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Table(name = "providers")
@Data
@NoArgsConstructor
@AllArgsConstructor
// Esta anotación es necesaria en ambos lados de la relación ManyToMany
@JsonIdentityInfo(
    generator = ObjectIdGenerators.PropertyGenerator.class,
    property = "id"
)
public class Provider {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String taxId;

    @Column(nullable = false)
    private String name;

    private String phone;

    @ManyToMany(mappedBy = "providers")
    private List<Product> products;
}