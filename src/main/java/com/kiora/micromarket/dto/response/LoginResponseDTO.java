package com.kiora.micromarket.dto.response;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class LoginResponseDTO {
    private String message;
    private String jwt;
    private LocalDateTime entryDate;
}
