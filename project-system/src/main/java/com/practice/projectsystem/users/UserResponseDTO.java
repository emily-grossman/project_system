package com.practice.projectsystem.users;

import com.practice.projectsystem.roles.Role;

import java.time.LocalDateTime;
import java.util.Set;

public record UserResponseDTO(
        Long id,
        String name,
        String surname,
        String email,
        Set<Role> roles,
        LocalDateTime registrationDate
)
{
}
