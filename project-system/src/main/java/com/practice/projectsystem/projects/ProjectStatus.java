package com.practice.projectsystem.projects;

import com.fasterxml.jackson.annotation.JsonValue;

public enum ProjectStatus {
    IN_PROGRESS("В работе"),
    COMPLETED("Завершен"),
    CANCELLED("Отменен");

    private final String rusName;


    ProjectStatus(String rusName) {
        this.rusName = rusName;
    }

    @JsonValue
    public String getRusName() {
        return rusName;
    }
}
