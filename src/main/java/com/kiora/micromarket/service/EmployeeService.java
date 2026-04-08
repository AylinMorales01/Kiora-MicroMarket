package com.kiora.micromarket.service;

import com.kiora.micromarket.dto.response.EmployeeResponseDTO;
import com.kiora.micromarket.dto.response.MessageResponseDTO;
import com.kiora.micromarket.entity.Employee;
import com.kiora.micromarket.entity.Role;
import com.kiora.micromarket.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import com.kiora.micromarket.dto.request.EmployeeRequestDTO;
import java.util.stream.Collectors;
@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final org.springframework.security.crypto.password.PasswordEncoder passwordEncoder;

    /**
     * Método para registrar un nuevo empleado
     * @param request EmployeeRequestDTO
     * @return MessageResponseDTO
     */
    public MessageResponseDTO create(EmployeeRequestDTO request) {
        MessageResponseDTO response = new MessageResponseDTO();

        Optional<Employee> employeeExist = employeeRepository.findByCedula(request.getCedula());

        if (employeeExist.isPresent()) {
            return new MessageResponseDTO("Un empleado con esta cédula ya está registrado");
        }

        if (request.getPassword() == null || request.getPassword().isBlank()) {
            return new MessageResponseDTO("La contraseña es obligatoria");
        }

        Employee employee = Employee.builder()
        .cedula(request.getCedula())
        .name(request.getName())
        .password(passwordEncoder.encode(request.getPassword()))
        .role(request.getRole())
        .entryDate(request.getEntryDate())
        .salary(request.getSalary())
        .active(true)
        .build();

        employeeRepository.save(employee);
        return new MessageResponseDTO("Empleado registrado exitosamente");
    }

    /**
     * Método para editar un empleado existente
     * @param id Long
     * @param request EmployeeRequestDTO
     * @return MessageResponseDTO
     */
    @org.springframework.transaction.annotation.Transactional
    public MessageResponseDTO update(Long id, EmployeeRequestDTO request) {
        return employeeRepository.findById(id)
                .filter(employee -> employee.isActive())
                .map(employee -> {
                    if (request.getCedula() != null) employee.setCedula(request.getCedula());
                    if (request.getName() != null) employee.setName(request.getName());
                    if (request.getPassword() != null && !request.getPassword().isBlank()) {
                        employee.setPassword(passwordEncoder.encode(request.getPassword()));
                    }
                    if (request.getRole() != null) employee.setRole(request.getRole());
                    if (request.getEntryDate() != null) employee.setEntryDate(request.getEntryDate());
                    if (request.getSalary() != null) employee.setSalary(request.getSalary());

                    employeeRepository.save(employee);
                    return new MessageResponseDTO("Empleado actualizado exitosamente");
                })
                .orElse(new MessageResponseDTO("Empleado no encontrado o inactivo"));

    }

    /**
     * Método para eliminar de forma lógica un empleado
     * @param id Long
     * @return MessageResponseDTO
     */
    public MessageResponseDTO delete(Long id) {
        return employeeRepository.findById(id)
                .filter(employee -> employee.isActive()) // Solo procesar si está activo
                .map(employee -> {
                    employee.setActive(false);
                    employeeRepository.save(employee);
                    return new MessageResponseDTO("Empleado eliminado exitosamente");
                })
                .orElse(new MessageResponseDTO("Empleado no encontrado"));
    }
    /**
     * Método para buscar un empleado por ID
     * @param id Long
     * @return EmployeeResponseDTO
     */
    public EmployeeResponseDTO findById(Long id) {
        return employeeRepository.findById(id)
                .filter(employee -> employee.isActive())
                .map(this::mapToDTO)
                .orElseThrow(() -> new RuntimeException("Empleado no encontrado o inactivo"));
    }

    /**
     * Método para listar todos los empleados activos
     * @return List<EmployeeResponseDTO>
     */
    public List<EmployeeResponseDTO> findAll() {
        return employeeRepository.findAll()
                .stream()
                .filter(employee -> employee.isActive())
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Método para filtrar empleados activos por rol (cargo)
     * @param role Role
     * @return List<EmployeeResponseDTO>
     */
    public List<EmployeeResponseDTO> findByRole(Role role) {
        return employeeRepository.findByRole(role)
                .stream()
                .filter(employee -> employee.isActive())
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Método para filtrar empleados activos por rango de fechas
     * @param start LocalDate
     * @param end LocalDate
     * @return List<EmployeeResponseDTO>
     */
    public List<EmployeeResponseDTO> findByDateRange(LocalDate start, LocalDate end) {
        return employeeRepository.findByEntryDateBetween(start, end)
                .stream()
                .filter(employee -> employee.isActive())
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    private EmployeeResponseDTO mapToDTO(Employee employee) {
        return EmployeeResponseDTO.builder()
                .id(employee.getId())
                .cedula(employee.getCedula())
                .name(employee.getName())
                .role(employee.getRole())
                .entryDate(employee.getEntryDate())
                .salary(employee.getSalary())
                .message("Ok")
                .build();
    }
}
