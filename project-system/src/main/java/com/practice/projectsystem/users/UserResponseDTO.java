package com.practice.projectsystem.users;

import com.practice.projectsystem.roles.Role;

import java.time.LocalDateTime;
import java.util.Set;

public record UserResponseDTO(
        Long id,
        String name,
        String surname,
        String patronymic,
        String email,
        String department,
        Set<Role> roles,
        LocalDateTime registrationDate
)
{
}
