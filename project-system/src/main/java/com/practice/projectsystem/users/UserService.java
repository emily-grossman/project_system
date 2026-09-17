package com.practice.projectsystem.users;

import com.practice.projectsystem.security.JwtTokenProvider;
import com.practice.projectsystem.roles.RoleEntity;
import com.practice.projectsystem.roles.RoleRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;


    public UserService(UserRepository userRepository, UserMapper userMapper, RoleRepository roleRepository, PasswordEncoder passwordEncoder, JwtTokenProvider jwtTokenProvider) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
    }


    @Transactional
    public UserResponseDTO registerUser(@Valid UserRequestDTO userToCreate) {
        var userToSave = userMapper.toEntity(userToCreate);
        String hashedPassword = passwordEncoder.encode(userToCreate.password());
        userToSave.setPassword(hashedPassword);

        Set<RoleEntity> roles = new HashSet<>();
        for (Long roleId : userToCreate.roleIds()) {
            RoleEntity role = roleRepository.findById(roleId)
                    .orElseThrow(() -> new IllegalStateException("Роль с id = " + roleId + "не найдена"));
            roles.add(role);
        }
        userToSave.setRoles(roles);

        var savedEntity =userRepository.save(userToSave);
        return userMapper.toDomain(savedEntity);
    }

    public LoginResponseDTO loginUser(@Valid LoginRequestDTO userToLogin) {
        UserEntity user = userRepository.findByEmail(userToLogin.email())
                .orElseThrow(() -> new IllegalArgumentException("Пользователь с email: " + userToLogin.email() + " не найден"));

        if (!passwordEncoder.matches(userToLogin.password(), user.getPassword())) {
            throw new IllegalArgumentException("Неверный пароль");
        }

        Set<String> roles = user.getRoles().stream()
                .map(RoleEntity::getName)
                .collect(Collectors.toSet());

        String token = jwtTokenProvider.createToken(userToLogin.email(), roles);
        return new LoginResponseDTO(token, userMapper.toDomain(user));
    }
}
