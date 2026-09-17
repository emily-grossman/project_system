package com.practice.projectsystem.projects;

import com.practice.projectsystem.accesses.ProjectAccessRequestDTO;
import com.practice.projectsystem.accesses.ProjectAccessResponseDTO;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/projects")
public class ProjectController {

    private final ProjectService projectService;
    private static final Logger log = LoggerFactory.getLogger(ProjectController.class);

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @PostMapping("/create")
    public ResponseEntity<ProjectResponseDTO> createProject(
            @RequestBody @Valid ProjectRequestDTO projectToCreate
    ) {
        log.info("Called method createProject");
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(projectService.createProject(projectToCreate));
    }

    @PostMapping("/allow")
    public ResponseEntity<ProjectAccessResponseDTO> allowAccess(
            @RequestBody @Valid ProjectAccessRequestDTO accessToAllow
    ) {
        log.info("Called allowAccess for projectId={}, userId={}",
                accessToAllow.projectId(), accessToAllow.userId());
        return ResponseEntity.status(HttpStatus.OK)
                .body(projectService.allowAccess(accessToAllow));
    }
}
