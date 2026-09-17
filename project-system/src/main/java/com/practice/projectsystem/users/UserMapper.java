package com.practice.projectsystem.users;

import com.practice.projectsystem.roles.Role;
import com.practice.projectsystem.roles.RoleMapper;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class UserMapper {

    private final RoleMapper roleMapper;

    public UserMapper(RoleMapper roleMapper) {
        this.roleMapper = roleMapper;
    }


    public UserResponseDTO toDomain(
            UserEntity user
    ){
        Set<Role> roles = user.getRoles().stream()
                .map(roleMapper::toDomain)
                .collect(Collectors.toSet());

        return new UserResponseDTO(
                user.getUuid(),
                user.getName(),
                user.getSurname(),
                user.getPatronymic(),
                user.getEmail(),
                user.getDepartment(),
                roles,
                user.getRegistrationDate()
        );
    }

    public UserEntity toEntity(
            UserRequestDTO user
    ){
        return new UserEntity(
                user.uuid(),
                user.name(),
                user.surname(),
                user.patronymic(),
                user.department(),
                user.email()
        );
    }

}
