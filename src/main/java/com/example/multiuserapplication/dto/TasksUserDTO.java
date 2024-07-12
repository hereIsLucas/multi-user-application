package com.example.multiuserapplication.dto;


import lombok.Builder;
import lombok.RequiredArgsConstructor;

@Builder
public record TasksUserDTO(Long id, String username, String password,String salt, String role) {
    public String getUsername() {
        return null;
    }

    public CharSequence getPassword() {
        return null;
    }

    public Object getRoles() {
        return null;
    }
}
