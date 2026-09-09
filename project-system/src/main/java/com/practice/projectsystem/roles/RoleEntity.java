package com.practice.projectsystem.roles;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Table(name="roles")
@Entity
public class RoleEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 50)
    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Size(max = 255)
    @Column(name = "description")
    private String description;

    public RoleEntity() {

    }

    public RoleEntity(Long id, String name, String description) {
        this.id=id;
        this.name=name;
        this.description=description;
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
