package com.practice.projectsystem.accesses;

import com.fasterxml.jackson.annotation.JsonValue;

public enum ProjectAccessType {
    READ("Чтение"),
    EDIT("Корректировка");

    private final String rusName;


    ProjectAccessType(String rusName) {
        this.rusName = rusName;
    }

    @JsonValue
    public String getRusName() {
        return rusName;
    }
}
