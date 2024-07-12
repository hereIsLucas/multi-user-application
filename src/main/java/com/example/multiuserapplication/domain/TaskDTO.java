package com.example.multiuserapplication.domain;

import lombok.Builder;

@Builder
public record TaskDTO(Long id, String description, TasksUserDTO userDTO) {
}
