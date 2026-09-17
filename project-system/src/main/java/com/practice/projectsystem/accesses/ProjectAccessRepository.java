package com.practice.projectsystem.accesses;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ProjectAccessRepository extends JpaRepository<ProjectAccessEntity, Long> {

    @Query("SELECT pa FROM ProjectAccessEntity pa WHERE :projectId=pa.project.id AND :userId=pa.user.id")
    Optional<ProjectAccessEntity> findByProjectIdAndUserId(
            @Param("projectId") Long projectId,
            @Param("userId") Long userId
    );
}
