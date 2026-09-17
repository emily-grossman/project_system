package com.practice.projectsystem.projects;

import jakarta.validation.constraints.*;

import java.time.LocalDate;


public record ProjectRequestDTO(
        @Null
        Long id,

        @NotBlank
        @Size(max = 200)
        String projectName,

        @NotBlank
        @Size(max = 100)
        String department,

        @NotNull
        Long departmentHeadId,

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
