package com.kiora.micromarket.service;

import com.kiora.micromarket.dto.request.EmployeeRequestDTO;
import com.kiora.micromarket.dto.response.EmployeeResponseDTO;
import com.kiora.micromarket.dto.response.MessageResponseDTO;
import com.kiora.micromarket.entity.Employee;
import com.kiora.micromarket.entity.Role;
import com.kiora.micromarket.repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private EmployeeService employeeService;

    private EmployeeRequestDTO request;
    private Employee employee;

    @BeforeEach
    void setUp() {
        request = new EmployeeRequestDTO();
        request.setCedula("1001");
        request.setName("Juan Perez");
        request.setRole(Role.CAJERO);
        request.setEntryDate(LocalDate.now());
        request.setSalary(2000.0);

        employee = new Employee();
        employee.setId(1L);
        employee.setCedula("1001");
        employee.setName("Juan Perez");
        employee.setRole(Role.CAJERO);
        employee.setEntryDate(LocalDate.now());
        employee.setSalary(2000.0);
        employee.setActive(true);
    }

    @Test
    void create_WhenEmployeeDoesNotExist_ShouldReturnSuccess() {
        when(employeeRepository.findByCedula(request.getCedula())).thenReturn(Optional.empty());
        when(employeeRepository.save(any(Employee.class))).thenReturn(employee);

        MessageResponseDTO response = employeeService.create(request);

        assertEquals("Empleado registrado exitosamente", response.getMessage());
        verify(employeeRepository, times(1)).save(any(Employee.class));
    }

    @Test
    void create_WhenEmployeeExists_ShouldReturnError() {
        when(employeeRepository.findByCedula(request.getCedula())).thenReturn(Optional.of(employee));

        MessageResponseDTO response = employeeService.create(request);

        assertEquals("Un empleado con esta cédula ya está registrado", response.getMessage());
        verify(employeeRepository, never()).save(any(Employee.class));
    }

    @Test
    void delete_WhenEmployeeExists_ShouldSoftDelete() {
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));

        MessageResponseDTO response = employeeService.delete(1L);

        assertFalse(employee.isActive());
        assertEquals("Empleado eliminado exitosamente", response.getMessage());
        verify(employeeRepository, times(1)).save(employee);
    }

    @Test
    void findAll_ShouldReturnOnlyActiveEmployees() {
        Employee inactiveEmployee = new Employee();
        inactiveEmployee.setActive(false);
        
        when(employeeRepository.findAll()).thenReturn(List.of(employee, inactiveEmployee));

        List<EmployeeResponseDTO> result = employeeService.findAll();

        assertEquals(1, result.size());
        assertEquals("Juan Perez", result.get(0).getName());
    }

    @Test
    void findByRole_ShouldReturnFilteredActiveEmployees() {
        when(employeeRepository.findByRole(Role.CAJERO)).thenReturn(List.of(employee));

        List<EmployeeResponseDTO> result = employeeService.findByRole(Role.CAJERO);

        assertEquals(1, result.size());
        assertEquals(Role.CAJERO, result.get(0).getRole());
    }
}
