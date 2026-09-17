package com.practice.projectsystem.projects;


import com.practice.projectsystem.accesses.*;
import com.practice.projectsystem.users.UserEntity;
import com.practice.projectsystem.users.UserRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@Transactional
public class ProjectService {

    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;
    private final ProjectMapper projectMapper;
    private final ProjectAccessMapper projectAccessMapper;
    private final ProjectAccessRepository projectAccessRepository;

    public ProjectService(
            UserRepository userRepository,
            ProjectRepository projectRepository,
            ProjectMapper projectMapper,
            ProjectAccessMapper projectAccessMapper,
            ProjectAccessRepository projectAccessRepository
    ) {
        this.userRepository = userRepository;
        this.projectRepository = projectRepository;
        this.projectMapper = projectMapper;
        this.projectAccessMapper = projectAccessMapper;
        this.projectAccessRepository = projectAccessRepository;
    }

    public ProjectResponseDTO createProject(@Valid ProjectRequestDTO projectToCreate) {
        String email = Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication()).getName();

        UserEntity projectManager = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalStateException("Пользователь не найден"));

        boolean hasUserProjectManagerRole = projectManager.getRoles().stream()
                .anyMatch(role -> role.getName().equals("Руководитель проекта"));

        if (!hasUserProjectManagerRole) {
            throw new SecurityException("Только руководитель проекта может создавать проекты");
        }

        UserEntity departmentHead = userRepository.findById(projectToCreate.departmentHeadUuid())
                .orElseThrow(() -> new IllegalArgumentException("Начальник отдела не найден"));

        boolean hasUserDepartmentHeadRole = departmentHead.getRoles().stream()
                .anyMatch(role -> role.getName().equals("Начальник"));

        if (!hasUserDepartmentHeadRole) {
            throw new IllegalArgumentException("Выбранный пользователь не является начальником отдела");
        }

        var projectToSave = projectMapper.toEntity(projectToCreate);
        projectToSave.setDepartmentHead(departmentHead);
        projectToSave.setProjectManager(projectManager);
        projectToSave.setStatus(ProjectStatus.IN_PROGRESS);

        var savedProjectEntity = projectRepository.save(projectToSave);

        var projectAccessToSave = new ProjectAccessEntity(
                projectToSave,
                projectManager,
                ProjectAccessType.EDIT
        );
        
        projectAccessRepository.save(projectAccessToSave);

        return projectMapper.toDomain(savedProjectEntity);
    }


    public ProjectAccessResponseDTO allowAccess(@Valid ProjectAccessRequestDTO accessToAllow) {
        String email = Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication()).getName();

        UserEntity currentUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalStateException("Пользователь не найден"));

        boolean hasUserProjectManagerRole = currentUser.getRoles().stream()
                .anyMatch(role -> role.getName().equals("Руководитель проекта"));

        if(!hasUserProjectManagerRole) {
            throw new SecurityException("Только руководитель проекта может выдавать доступ");
        }

        ProjectEntity project = projectRepository.findById(accessToAllow.projectUuid())
                .orElseThrow(() -> new IllegalArgumentException("Проект не найден"));

        if (!project.getProjectManager().getUuid().equals(currentUser.getUuid())){
            throw new SecurityException("Вы не являетесь руководителем этого проекта");
        }

        if (accessToAllow.userUuid().equals(currentUser.getUuid())) {
            throw new IllegalArgumentException("Нельзя выдать доступ самому себе");
        }

        UserEntity targetUser = userRepository.findById(accessToAllow.userUuid())
                .orElseThrow(() -> new IllegalStateException("Пользователь не найден"));

        ProjectAccessEntity projectAccess = projectAccessRepository
                .findByProjectIdAndUserId(
                accessToAllow.projectUuid(),
                accessToAllow.userUuid())
                .map(existing -> {
                    existing.setProjectAccessType(accessToAllow.projectAccessType());
                    return existing;
                })
                .orElseGet(() -> new ProjectAccessEntity(
                        project,
                        targetUser,
                        accessToAllow.projectAccessType()
                ));


        var savedEntity = projectAccessRepository.save(projectAccess);
        return projectAccessMapper.toDomain(savedEntity);

    }
}
