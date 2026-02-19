package org.example.dto;

import lombok.Data;

// Request DTO
@Data
public class SignInRequest {
    private String phoneNumber;
    private String password;
}
