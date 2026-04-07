package com.kiora.micromarket.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "sales")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "details")
@EqualsAndHashCode(exclude = "details")
public class Sale {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime date;

    private double subtotal;
    private double iva;
    private double total;

    @Column(nullable = false)
    private boolean active = true;

    @ManyToOne(optional = false)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    @OneToMany(mappedBy = "sale", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SaleDetail> details = new ArrayList<>();

    public void addDetail(SaleDetail detail) {
        details.add(detail);
        detail.setSale(this);
    }

    public void removeDetail(SaleDetail detail) {
        details.remove(detail);
        detail.setSale(null);
    }
}
