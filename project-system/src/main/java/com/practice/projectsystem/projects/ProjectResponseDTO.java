package com.practice.projectsystem.projects;

import java.time.LocalDate;
import java.util.UUID;


public record ProjectResponseDTO(
        UUID uuid,
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
