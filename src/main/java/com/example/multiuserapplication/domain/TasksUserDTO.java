package com.example.multiuserapplication.domain;


import lombok.Builder;
import lombok.RequiredArgsConstructor;

@Builder
public record TasksUserDTO(Long id, String username, String password,String salt, String role) {
}
