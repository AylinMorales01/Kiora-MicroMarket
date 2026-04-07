package com.kiora.micromarket.controller;

import com.kiora.micromarket.dto.request.EmployeeRequestDTO;
import com.kiora.micromarket.dto.response.EmployeeResponseDTO;
import com.kiora.micromarket.dto.response.MessageResponseDTO;
import com.kiora.micromarket.entity.Role;
import com.kiora.micromarket.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/employees")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService service;

    @GetMapping("/get-all")
    public ResponseEntity<?> getAll() {
        try {
            List<EmployeeResponseDTO> response = service.findAll();
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            MessageResponseDTO errorResponse = new MessageResponseDTO();
            errorResponse.setMessage("Error al intentar obtener el listado de los empleados");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        try {
            EmployeeResponseDTO response = service.findById(id);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (RuntimeException e) {
            MessageResponseDTO errorResponse = new MessageResponseDTO();
            errorResponse.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
    }

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody EmployeeRequestDTO request) {
        try {
            MessageResponseDTO response = service.create(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (RuntimeException e) {
            MessageResponseDTO errorResponse = new MessageResponseDTO();
            errorResponse.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody EmployeeRequestDTO request) {
        try {
            MessageResponseDTO response = service.update(id, request);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (RuntimeException e) {
            MessageResponseDTO errorResponse = new MessageResponseDTO();
            errorResponse.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        try {
            MessageResponseDTO response = service.delete(id);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (RuntimeException e) {
            MessageResponseDTO errorResponse = new MessageResponseDTO();
            errorResponse.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
    }

    @GetMapping("/filter/cargo")
    public ResponseEntity<?> findByRole(@RequestParam Role cargo) {
        try {
            List<EmployeeResponseDTO> response = service.findByRole(cargo);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            MessageResponseDTO errorResponse = new MessageResponseDTO();
            errorResponse.setMessage("Error al filtrar por cargo");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @GetMapping("/filter/date")
    public ResponseEntity<?> findByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end) {
        try {
            List<EmployeeResponseDTO> response = service.findByDateRange(start, end);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            MessageResponseDTO errorResponse = new MessageResponseDTO();
            errorResponse.setMessage("Error al filtrar por rango de fechas");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
}
