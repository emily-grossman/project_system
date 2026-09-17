package com.practice.projectsystem.users;

import jakarta.validation.constraints.*;

import java.util.Set;


public record UserRequestDTO(
        @Null
        Long id,

        @NotBlank
        @Size(max = 50)
        String name,

        @NotBlank
        @Size(max = 50)
        String surname,

        @Size(max = 50)
        String patronymic,

        @NotBlank
        @Size(max = 100)
        String department,

        @NotBlank
        @Email
        @Size(max = 255)
        String email,

        @NotEmpty
        Set<@NotNull Long> roleIds,

        @NotBlank
        @Size(min = 8, max = 20)
        String password
) {

}
