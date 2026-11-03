package com.universidad.mediconnect2.auth;

import java.time.Clock;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Locale;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import com.universidad.mediconnect2.shared.ConflictException;

@Service
public class AuthService {
    private final UserAccountRepository users;
    private final PasswordEncoder passwords;
    private final JwtEncoder jwtEncoder;
    private final Clock clock;
    private final long expiryMinutes;

    public AuthService(UserAccountRepository users, PasswordEncoder passwords, JwtEncoder jwtEncoder,
                       Clock clock, @Value("${app.security.jwt-expiration-minutes:60}") long expiryMinutes) {
        this.users=users; this.passwords=passwords; this.jwtEncoder=jwtEncoder;
        this.clock=clock; this.expiryMinutes=expiryMinutes;
    }

    @Transactional
    public UserAccount register(String email, String password, String firstName, String lastName) {
        String normalized = email.trim().toLowerCase(Locale.ROOT);
        if (users.existsByEmailIgnoreCase(normalized)) throw new ConflictException("El correo ya esta registrado.");
        return users.save(new UserAccount(normalized, passwords.encode(password),
                firstName.trim(), lastName.trim(), UserRole.ROLE_PATIENT));
    }

    @Transactional(readOnly=true)
    public String login(String email, String password) {
        UserAccount user = users.findByEmailIgnoreCase(email.trim())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Credenciales invalidas"));
        if (!passwords.matches(password, user.getPasswordHash())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Credenciales invalidas");
        }
        Instant now=clock.instant();
        JwtClaimsSet claims=JwtClaimsSet.builder()
                .issuer("mediconnect2")
                .subject(user.getEmail())
                .issuedAt(now)
                .expiresAt(now.plus(expiryMinutes, ChronoUnit.MINUTES))
                .claim("userId", user.getId().toString())
                .claim("roles", List.of(user.getRole().name()))
                .build();
        return jwtEncoder.encode(JwtEncoderParameters.from(
                JwsHeader.with(MacAlgorithm.HS512).build(), claims)).getTokenValue();
    }
}
