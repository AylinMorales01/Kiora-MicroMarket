package com.kiora.micromarket.dto.request;

import lombok.Data;

import java.time.LocalDate;

@Data
public class LoginRequestDTO {
    private String cedula;
    private String password;
}
