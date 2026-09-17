package com.practice.projectsystem.projects;


import com.practice.projectsystem.accesses.*;
import com.practice.projectsystem.users.UserEntity;
import com.practice.projectsystem.users.UserRepository;
import com.practice.projectsystem.users.UserService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.UUID;

@Service
@Transactional
public class ProjectService {

    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;
    private final ProjectMapper projectMapper;
    private final ProjectAccessMapper projectAccessMapper;
    private final ProjectAccessRepository projectAccessRepository;
    private final UserService userService;

    public ProjectService(
            UserRepository userRepository,
            ProjectRepository projectRepository,
            ProjectMapper projectMapper,
            ProjectAccessMapper projectAccessMapper,
            ProjectAccessRepository projectAccessRepository,
            UserService userService) {
        this.userRepository = userRepository;
        this.projectRepository = projectRepository;
        this.projectMapper = projectMapper;
        this.projectAccessMapper = projectAccessMapper;
        this.projectAccessRepository = projectAccessRepository;
        this.userService = userService;
    }

    public ProjectResponseDTO createProject(@Valid ProjectRequestDTO projectToCreate) {
        String email = Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication()).getName();

        UserEntity projectManager = userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Пользователь не найден"));

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
                .orElseThrow(() -> new EntityNotFoundException("Пользователь не найден"));

        boolean hasUserProjectManagerRole = currentUser.getRoles().stream()
                .anyMatch(role -> role.getName().equals("Руководитель проекта"));

        if(!hasUserProjectManagerRole) {
            throw new SecurityException("Только руководитель проекта может выдавать доступ");
        }

        ProjectEntity project = projectRepository.findById(accessToAllow.projectUuid())
                .orElseThrow(() -> new EntityNotFoundException("Проект не найден"));

        if (!project.getProjectManager().getUuid().equals(currentUser.getUuid())){
            throw new SecurityException("Вы не являетесь руководителем этого проекта");
        }

        if (accessToAllow.userUuid().equals(currentUser.getUuid())) {
            throw new IllegalArgumentException("Нельзя выдать доступ самому себе");
        }

        UserEntity targetUser = userRepository.findById(accessToAllow.userUuid())
                .orElseThrow(() -> new EntityNotFoundException("Пользователь не найден"));

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

    public ProjectResponseDTO getProjectInfo(@Valid UUID projectUuid) throws AccessDeniedException {

        String email = Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication()).getName();

        UserEntity currentUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Пользователь не найден"));

        ProjectEntity project = projectRepository.findById(projectUuid)
                .orElseThrow(() -> new EntityNotFoundException("Проект не найден"));

        checkProjectAccess(project, currentUser);

        return projectMapper.toDomain(project);

    }

    private void checkProjectAccess(ProjectEntity project, UserEntity user) throws AccessDeniedException {
        if (isProjectManagerOf(project, user)){
            return;
        }
        if (isDepartmentHeadOf(project, user)){
            return;
        }
        if (hasDirectAccess(project.getUuid(), user.getUuid())){
            return;
        }
        throw new AccessDeniedException("У вас нет доступа к этому проекту");

    }

    private boolean isProjectManagerOf(ProjectEntity project, UserEntity user) {
        boolean isSameUser = (project.getProjectManager().getUuid().equals(user.getUuid()));
        boolean hasProjectManagerRole = user.getRoles().stream()
                .anyMatch(role -> "Руководитель проекта".equals(role.getName()));
        return isSameUser && hasProjectManagerRole;
    }

    private boolean isDepartmentHeadOf(ProjectEntity project, UserEntity user) {
        boolean isSameUser = (project.getDepartmentHead().getUuid().equals(user.getUuid()));
        boolean hasDepartmentHeadRole = user.getRoles().stream()
                .anyMatch(role -> "Начальник".equals(role.getName()));
        return isSameUser && hasDepartmentHeadRole;
    }

    private boolean hasDirectAccess(UUID projectUuid, UUID userUuid) {
        return projectAccessRepository.findByProjectIdAndUserId(projectUuid, userUuid).isPresent();
    }

    @Transactional
    public ProjectResponseDTO editProject(
            @Valid UUID projectUuid,
            @Valid ProjectUpdateRequestDTO projectToUpdate)
    {
        String email = Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication()).getName();

        UserEntity currentUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Пользователь не найден"));

        ProjectEntity project = projectRepository.findById(projectUuid)
                .orElseThrow(() -> new EntityNotFoundException("Проект не найден"));

        if (isProjectManagerOf(project, currentUser) || hasDirectAccess(projectUuid, currentUser.getUuid())){
            if (projectToUpdate.projectName() != null) {
                project.setProjectName(projectToUpdate.projectName());
            }

            if (projectToUpdate.startDate() != null){
                project.setStartDate(projectToUpdate.startDate());
            }
            if (projectToUpdate.endDate() != null){
                project.setEndDate(projectToUpdate.endDate());
            }
            if (projectToUpdate.customer() != null){
                project.setCustomer(projectToUpdate.customer());
            }
            if (projectToUpdate.description() != null) {
                project.setDescription(projectToUpdate.description());
            }
            if (projectToUpdate.status() != null) {
                project.setStatus(projectToUpdate.status());
            }

            var savedEntity = projectRepository.save(project);

            return projectMapper.toDomain(savedEntity);
        }
        throw new AccessDeniedException("У вас нет прав на редактирование этого проекта");

    }

}
