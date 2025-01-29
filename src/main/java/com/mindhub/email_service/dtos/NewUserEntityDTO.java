package com.mindhub.email_service.dtos;

import java.io.Serializable;

public class NewUserEntityDTO implements Serializable {
    private String username;
    private String email;
    private String role;

    NewUserEntityDTO() {}

    public NewUserEntityDTO(String username, String email, String role) {
        this.username = username;
        this.email = email;
        this.role = role;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getRole() {
        return role;
    }
}
