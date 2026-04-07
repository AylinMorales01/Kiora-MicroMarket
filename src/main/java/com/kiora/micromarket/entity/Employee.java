    package com.kiora.micromarket.entity;


    import jakarta.persistence.*;
    import lombok.AllArgsConstructor;
    import lombok.Builder;
    import lombok.Data;
    import lombok.NoArgsConstructor;
    import java.time.LocalDate;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Entity
    @Table(name = "employees")
    public class Employee {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @Column(nullable = false, unique = true)
        private String cedula;

        @Column(nullable = false)
        private String name;

        @Enumerated(EnumType.STRING)
        @Column(nullable = false)
        private Role role;

        @Column(name = "entry_date")
        private LocalDate entryDate;

        @Column(nullable = false)
        private Double salary;

        @Builder.Default
        @Column(nullable = false)
        private boolean active = true;

    }
