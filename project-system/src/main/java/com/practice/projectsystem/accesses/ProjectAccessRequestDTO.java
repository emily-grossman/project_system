package com.practice.projectsystem.accesses;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record ProjectAccessRequestDTO(
        @NotNull
        UUID projectUuid,

        @NotNull
        UUID userUuid,

        @NotNull
        ProjectAccessType projectAccessType
) {
}
