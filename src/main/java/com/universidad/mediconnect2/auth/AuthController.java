package com.universidad.mediconnect2.auth;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final AuthService auth;
    public AuthController(AuthService auth) { this.auth=auth; }

    public record RegisterRequest(@NotBlank @Email String email, @NotBlank @Size(min=8) String password,
                                  @NotBlank String firstName, @NotBlank String lastName) {}
    public record LoginRequest(@NotBlank String email, @NotBlank String password) {}
    public record RegisterResponse(String id, String email, String role) {}
    public record LoginResponse(String accessToken, String tokenType) {}

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public RegisterResponse register(@Valid @RequestBody RegisterRequest request) {
        UserAccount user=auth.register(request.email(), request.password(), request.firstName(), request.lastName());
        return new RegisterResponse(user.getId().toString(), user.getEmail(), user.getRole().name());
    }

    @PostMapping("/login")
    public LoginResponse login(@Valid @RequestBody LoginRequest request) {
        return new LoginResponse(auth.login(request.email(), request.password()), "Bearer");
    }
}
