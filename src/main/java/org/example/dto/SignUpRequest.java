package org.example.dto;

import lombok.Data;

@Data
public class SignUpRequest {
    private String phoneNumber;
    private String password;
    private String name;
    private String orgName;
    private String confirmPassword;
}
