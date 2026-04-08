package com.kiora.micromarket.repository;

import com.kiora.micromarket.entity.Employee;
import com.kiora.micromarket.entity.Employee.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    
    Optional<Employee> findByCedula(String cedula);
    
    List<Employee> findByRole(Role role);
    
    List<Employee> findByEntryDateBetween(LocalDate start, LocalDate end);
}
