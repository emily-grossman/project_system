package com.practice.projectsystem.projects;

import jakarta.validation.constraints.*;

import java.time.LocalDate;
import java.util.UUID;


public record ProjectRequestDTO(
        @Null
        UUID uuid,

        @NotBlank
        @Size(max = 200)
        String projectName,

        @NotBlank
        @Size(max = 100)
        String department,

        @NotNull
        UUID departmentHeadUuid,

        @NotNull
        @FutureOrPresent
        LocalDate startDate,

        @NotNull
        @Future
        LocalDate endDate,

        @NotBlank
        @Size(max = 200)
        String customer,


        @Size(max = 1000)
        String description

) {
}
