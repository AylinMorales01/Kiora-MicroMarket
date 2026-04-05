package com.kiora.micromarket.service;

import com.kiora.micromarket.dto.request.EmployeeRequestDTO;
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
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    /**
     * Método para registrar un nuevo empleado
     * @param request EmployeeRequestDTO
     * @return MessageResponseDTO
     */
    public MessageResponseDTO create(EmployeeRequestDTO request) {
        MessageResponseDTO response = new MessageResponseDTO();

        Optional<Employee> employeeExist = employeeRepository.findByCedula(request.getCedula());

        if (employeeExist.isPresent()) {
            response.setMessage("Un empleado con esta cédula ya está registrado");
            return response;
        }

        Employee employee = new Employee();
        employee.setCedula(request.getCedula());
        employee.setName(request.getName());
        employee.setRole(request.getRole());
        employee.setEntryDate(request.getEntryDate());
        employee.setSalary(request.getSalary());
        employee.setActive(true);

        employeeRepository.save(employee);

        response.setMessage("Empleado registrado exitosamente");
        return response;
    }

    /**
     * Método para editar un empleado existente
     * @param id Long
     * @param request EmployeeRequestDTO
     * @return MessageResponseDTO
     */
    public MessageResponseDTO update(Long id, EmployeeRequestDTO request) {
        MessageResponseDTO response = new MessageResponseDTO();

        Optional<Employee> employeeOptional = employeeRepository.findById(id);

        if (employeeOptional.isEmpty()) {
            response.setMessage("Empleado no encontrado");
            return response;
        }

        Employee employee = employeeOptional.get();
        employee.setCedula(request.getCedula());
        employee.setName(request.getName());
        employee.setRole(request.getRole());
        employee.setEntryDate(request.getEntryDate());
        employee.setSalary(request.getSalary());

        employeeRepository.save(employee);

        response.setMessage("Empleado actualizado exitosamente");
        return response;
    }

    /**
     * Método para eliminar de forma lógica un empleado
     * @param id Long
     * @return MessageResponseDTO
     */
    public MessageResponseDTO delete(Long id) {
        MessageResponseDTO response = new MessageResponseDTO();

        Optional<Employee> employeeOptional = employeeRepository.findById(id);

        if (employeeOptional.isEmpty()) {
            response.setMessage("Empleado no encontrado");
            return response;
        }

        Employee employee = employeeOptional.get();
        employee.setActive(false);
        employeeRepository.save(employee);

        response.setMessage("Empleado eliminado exitosamente");
        return response;
    }

    /**
     * Método para listar todos los empleados activos
     * @return List<EmployeeResponseDTO>
     */
    public List<EmployeeResponseDTO> findAll() {
        return employeeRepository.findAll()
                .stream()
                .filter(Employee::isActive)
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Método para filtrar empleados activos por cargo
     * @param role Role
     * @return List<EmployeeResponseDTO>
     */
    public List<EmployeeResponseDTO> findByRole(Role role) {
        return employeeRepository.findByRole(role)
                .stream()
                .filter(Employee::isActive)
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
                .filter(Employee::isActive)
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    private EmployeeResponseDTO mapToDTO(Employee employee) {
        EmployeeResponseDTO dto = new EmployeeResponseDTO();
        dto.setId(employee.getId());
        dto.setCedula(employee.getCedula());
        dto.setName(employee.getName());
        dto.setRole(employee.getRole());
        dto.setEntryDate(employee.getEntryDate());
        dto.setSalary(employee.getSalary());
        dto.setMessage("Ok");
        return dto;
    }
}
