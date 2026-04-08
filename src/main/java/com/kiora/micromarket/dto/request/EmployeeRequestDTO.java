package com.kiora.micromarket.dto.request;

import com.kiora.micromarket.entity.Role;
import lombok.Data;
import lombok.Builder;

import java.time.LocalDate;

@Builder
@Data
public class EmployeeRequestDTO {
    private String cedula;
    private String name;
    private String password;
    private Role role;
    private Double salary;
    private LocalDate entryDate;
}
