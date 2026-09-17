package com.practice.projectsystem.accesses;

import org.springframework.stereotype.Component;

@Component
public class ProjectAccessMapper {

    public ProjectAccessResponseDTO toDomain(ProjectAccessEntity projectAccess) {
        return new ProjectAccessResponseDTO(
                projectAccess.getId(),
                projectAccess.getProject().getId(),
                projectAccess.getProject().getProjectName(),
                projectAccess.getUser().getId(),
                projectAccess.getUser().getEmail(),
                projectAccess.getProjectAccessType(),
                projectAccess.getGrantedAt()
        );

    }

}
