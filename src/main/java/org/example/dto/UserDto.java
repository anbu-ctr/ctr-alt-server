package org.example.dto;

import lombok.Data;

@Data
public class UserDto {
    private Integer id;
    private String phoneNumber;
    private String createdAt;
    private String name;
    private String email;
    private String orgName;

    public UserDto(final Integer id, final String phoneNumber, final String createdAt, final String name, final String email, final String orgName) {
        this.id = id;
        this.phoneNumber = phoneNumber;
        this.createdAt = createdAt;
        this.name = name;
        this.email = email;
        this.orgName = orgName;
    }
}

