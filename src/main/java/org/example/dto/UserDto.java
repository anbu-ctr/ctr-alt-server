package org.example.dto;

import lombok.Data;

@Data
public class UserDto {
    private Integer id;
    private String phoneNumber;
    private String createdAt;
    private String name;

    public UserDto(final Integer id, final String phoneNumber, final String createdAt, final String name) {
        this.id = id;
        this.phoneNumber = phoneNumber;
        this.createdAt = createdAt;
        this.name = name;
    }
}

