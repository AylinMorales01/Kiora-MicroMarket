package com.kiora.micromarket.dto.response;

import com.kiora.micromarket.entity.Role;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDate;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeResponseDTO {
    private Long id;
    private String cedula;
    private String name;
    private Role role;
    private LocalDate entryDate;
    private Double salary;
    private String message;
}
