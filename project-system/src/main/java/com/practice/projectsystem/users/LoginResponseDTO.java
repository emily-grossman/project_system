package com.practice.projectsystem.users;

public record LoginResponseDTO(
        String accessToken,
        String tokenType,
        UserResponseDTO user
) {
    public LoginResponseDTO(String accessToken, UserResponseDTO user){
        this(accessToken, "Bearer", user);
    }
}
