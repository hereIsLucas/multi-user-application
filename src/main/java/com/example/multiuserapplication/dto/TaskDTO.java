package com.example.multiuserapplication.dto;

import lombok.Builder;

@Builder
public record TaskDTO(Long id, String description, TasksUserDTO userDTO) {
}
