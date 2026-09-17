package com.practice.projectsystem.accesses;

import java.time.LocalDateTime;

public record ProjectAccessResponseDTO(
        Long id,
        Long projectId,
        String projectName,
        Long userId,
        String userEmail,
        ProjectAccessType projectAccessType,
        LocalDateTime grantedAt
) {
}
