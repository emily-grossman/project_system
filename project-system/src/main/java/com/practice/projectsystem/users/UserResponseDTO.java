package com.practice.projectsystem.users;

import com.practice.projectsystem.roles.Role;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

public record UserResponseDTO(
        UUID uuid,
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
