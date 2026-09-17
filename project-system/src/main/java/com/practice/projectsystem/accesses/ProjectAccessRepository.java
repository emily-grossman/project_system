package com.practice.projectsystem.accesses;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface ProjectAccessRepository extends JpaRepository<ProjectAccessEntity, Long> {

    @Query("SELECT pa FROM ProjectAccessEntity pa WHERE :projectUuid=pa.project.uuid AND :userUuid=pa.user.uuid")
    Optional<ProjectAccessEntity> findByProjectIdAndUserId(
            @Param("projectUuid") UUID projectUuid,
            @Param("userUuid") UUID userUuid
    );
}
