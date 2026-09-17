package com.practice.projectsystem.accesses;

import com.practice.projectsystem.projects.ProjectEntity;
import com.practice.projectsystem.users.UserEntity;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name="project_access", uniqueConstraints = @UniqueConstraint(columnNames = {"project_uuid", "user_uuid"}))
public class ProjectAccessEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_uuid", nullable = false)
    private ProjectEntity project;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_uuid", nullable = false)
    private UserEntity user;

    @Enumerated(EnumType.STRING)
    @Column(name = "project_access_type")
    private ProjectAccessType projectAccessType;

    @CreationTimestamp
    @Column(name = "granted_at", updatable = false)
    private LocalDateTime grantedAt;

    public ProjectAccessEntity() {

    }

    public ProjectAccessEntity(
            ProjectEntity project,
            UserEntity user,
            ProjectAccessType projectAccessType
    ) {
        this.project=project;
        this.user=user;
        this.projectAccessType=projectAccessType;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ProjectEntity getProject() {
        return project;
    }

    public void setProject(ProjectEntity project) {
        this.project = project;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public ProjectAccessType getProjectAccessType() {
        return projectAccessType;
    }

    public void setProjectAccessType(ProjectAccessType projectAccessType) {
        this.projectAccessType = projectAccessType;
    }

    public LocalDateTime getGrantedAt() {
        return grantedAt;
    }
}
