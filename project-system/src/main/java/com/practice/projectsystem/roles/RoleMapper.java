package com.practice.projectsystem.roles;

import org.springframework.stereotype.Component;

@Component
public class RoleMapper {

    public Role toDomain(
            RoleEntity role
    ) {
        return new Role(
                role.getId(),
                role.getName(),
                role.getDescription()
        );
    }

    public RoleEntity toEntity(
            Role role
    ) {
        return new RoleEntity(
                role.id(),
                role.name(),
                role.description()
        );
    }
}
