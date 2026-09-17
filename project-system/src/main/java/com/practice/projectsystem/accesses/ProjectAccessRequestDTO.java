package com.practice.projectsystem.accesses;

import jakarta.validation.constraints.NotNull;

public record ProjectAccessRequestDTO(
        @NotNull
        Long projectId,

        @NotNull
        Long userId,

        @NotNull
        ProjectAccessType projectAccessType
) {
}
