package com.practice.projectsystem.users;


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
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponseDTO> registerUser(
            @RequestBody @Valid UserRequestDTO userToCreate
    ) {
        log.info("Called method registerUser");
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(userService.registerUser(userToCreate));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> loginUser(
            @RequestBody @Valid LoginRequestDTO userToLogin
    ) {
        log.info("Called method loginUser with email={}", userToLogin.email());
        return ResponseEntity.status(HttpStatus.OK)
                .body(userService.loginUser(userToLogin));
    }
}
