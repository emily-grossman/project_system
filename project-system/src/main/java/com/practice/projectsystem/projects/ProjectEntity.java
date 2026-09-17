package com.practice.projectsystem.projects;

import com.practice.projectsystem.users.UserEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.time.LocalDate;
import java.util.UUID;


@Table(name = "projects")
@Entity
public class ProjectEntity {

    @Id
    @Column(name = "uuid", nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID uuid;

    @NotBlank
    @Size(max = 200)
    @Column(name = "project_name", nullable = false)
    private String projectName;

    @NotBlank
    @Size(max = 100)
    @Column(name = "department", nullable = false)
    private String department;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_head_uuid", nullable = false)
    private UserEntity departmentHead;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_manager_id", nullable = false)
    private UserEntity projectManager;

    @NotBlank
    @Size(max = 200)
    @Column(name = "customer", nullable = false)
    private String customer;

    @Size(max = 1000)
    @Column(name = "description")
    private String description;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ProjectStatus status;

    public ProjectEntity() {

    }

    public ProjectEntity(
            UUID uuid,
            String projectName,
            String department,
            UserEntity departmentHead,
            UserEntity projectManager,
            String customer,
            String description,
            LocalDate startDate,
            LocalDate endDate,
            ProjectStatus status
    ) {
        this.uuid = uuid;
        this.projectName = projectName;
        this.department = department;
        this.departmentHead = departmentHead;
        this.projectManager=projectManager;
        this.customer=customer;
        this.description=description;
        this.startDate=startDate;
        this.endDate=endDate;
        this.status=status;
    }


    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public UserEntity getDepartmentHead() {
        return departmentHead;
    }

    public void setDepartmentHead(UserEntity departmentHead) {
        this.departmentHead = departmentHead;
    }

    public UserEntity getProjectManager() {
        return projectManager;
    }

    public void setProjectManager(UserEntity projectManager) {
        this.projectManager = projectManager;
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public ProjectStatus getStatus() {
        return status;
    }

    public void setStatus(ProjectStatus status) {
        this.status = status;
    }


    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }
}
