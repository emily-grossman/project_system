package com.practice.projectsystem.accesses;

import org.springframework.stereotype.Component;

@Component
public class ProjectAccessMapper {

    public ProjectAccessResponseDTO toDomain(ProjectAccessEntity projectAccess) {
        return new ProjectAccessResponseDTO(
                projectAccess.getId(),
                projectAccess.getProject().getUuid(),
                projectAccess.getProject().getProjectName(),
                projectAccess.getUser().getUuid(),
                projectAccess.getUser().getEmail(),
                projectAccess.getProjectAccessType(),
                projectAccess.getGrantedAt()
        );

    }

}
