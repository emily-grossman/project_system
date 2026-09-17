package com.practice.projectsystem.projects;

import java.time.LocalDate;


public record ProjectResponseDTO(
        Long id,
        String projectName,
        String department,
        String departmentHead,
        String projectManager,
        LocalDate startDate,
        LocalDate endDate,
        String customer,
        String description,
        ProjectStatus status
) {
}
