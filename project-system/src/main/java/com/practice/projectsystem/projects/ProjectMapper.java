package com.practice.projectsystem.projects;

import com.practice.projectsystem.users.UserEntity;
import com.practice.projectsystem.users.UserRepository;
import org.springframework.stereotype.Component;

@Component
public class ProjectMapper {

    public ProjectResponseDTO toDomain(ProjectEntity project) {
        return new ProjectResponseDTO(
                project.getId(),
                project.getProjectName(),
                project.getDepartment(),
                project.getDepartmentHead().getSurname() + " " + project.getDepartmentHead().getName() + " " + project.getDepartmentHead().getPatronymic(),
                project.getProjectManager().getSurname() + " " + project.getProjectManager().getName() + " " + project.getProjectManager().getPatronymic(),
                project.getStartDate(),
                project.getEndDate(),
                project.getCustomer(),
                project.getDescription(),
                project.getStatus()
        );
    }

    public ProjectEntity toEntity(
            ProjectRequestDTO project
    ) {
        return new ProjectEntity(
                project.id(),
                project.projectName(),
                project.department(),
                null,
                null,
                project.customer(),
                project.description(),
                project.startDate(),
                project.endDate(),
                null
        );
    }
}
