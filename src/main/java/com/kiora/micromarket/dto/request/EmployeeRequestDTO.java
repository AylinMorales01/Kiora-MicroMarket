package com.kiora.micromarket.dto.request;

import com.kiora.micromarket.entity.Role;
import lombok.Data;
import java.time.LocalDate;

@Data
public class EmployeeRequestDTO {
    private String cedula;
    private String name;
    private Role role;
    private LocalDate entryDate;
    private Double salary;
}
