package org.example.dto;

import lombok.Data;

// Response DTO
@Data
public class SignInResponse {
    private boolean success;
    private String message;
    private String token;
    private UserDto user;

    public SignInResponse(final boolean success, final String message) {
        this.success = success;
        this.message = message;
    }

    public SignInResponse(final boolean success, final String message, final String token, final UserDto user) {
        this.success = success;
        this.message = message;
        this.token = token;
        this.user = user;
    }
}
