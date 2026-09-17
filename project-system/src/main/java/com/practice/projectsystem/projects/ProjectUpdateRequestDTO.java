package com.practice.projectsystem.projects;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record ProjectUpdateRequestDTO(

        @Size(max = 200)
        String projectName,

        @FutureOrPresent
        LocalDate startDate,

        @Future
        LocalDate endDate,

        @Size(max = 200)
        String customer,

        @Size(max = 1000)
        String description,

        ProjectStatus status
) {
}
