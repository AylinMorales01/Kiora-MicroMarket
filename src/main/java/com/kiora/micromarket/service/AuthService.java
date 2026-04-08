package com.kiora.micromarket.service;

import java.util.Optional;
import java.time.LocalDateTime;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.kiora.micromarket.dto.request.LoginRequestDTO;
import com.kiora.micromarket.dto.response.LoginResponseDTO;
import com.kiora.micromarket.dto.response.MessageResponseDTO;
import com.kiora.micromarket.entity.Employee;
import com.kiora.micromarket.repository.EmployeeRepository;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final EmployeeRepository employeeRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    /**
    *  Método de login
    * @param request LoginRequestDTO
    * @return LoginResponseDTO
    */
    public LoginResponseDTO login(LoginRequestDTO request) {
        Optional<Employee> employeeOptional = employeeRepository.findByCedula(request.getCedula());
        LoginResponseDTO response = new LoginResponseDTO();

        if (employeeOptional.isEmpty()) {
            response.setMessage("Este Empleado no esta registrado");
            return response;
        }

        Employee employee = employeeOptional.get();

        if (!passwordEncoder.matches(request.getPassword(), employee.getPassword())) {
            throw new RuntimeException("Contraseña o Cédula incorrecta");
        }

        String token = jwtService.generateToken(employee.getId(), employee.getCedula());

        response.setMessage("Inicio de sesion exitoso");
        response.setJwt(token);
        
        // Convert LocalDate to LocalDateTime or just don't set it if not strictly required
        // Since entryDate is LocalDate in Employee, but LoginResponseDTO expects entryDate, we can set it.
        // Wait, LoginResponseDTO entryDate type might be different. We'll set it as string or pass it directly.
        response.setEntryDate(employee.getEntryDate() != null ? employee.getEntryDate().atStartOfDay() : null);
        return response;
    }
}

