package com.practice.projectsystem.accesses;

import java.time.LocalDateTime;
import java.util.UUID;

public record ProjectAccessResponseDTO(
        Long id,
        UUID projectUuid,
        String projectName,
        UUID userUuid,
        String userEmail,
        ProjectAccessType projectAccessType,
        LocalDateTime grantedAt
) {
}
