package com.universidad.mediconnect2.auth;

import java.util.UUID;
import jakarta.persistence.*;

@Entity
@Table(name="app_users", uniqueConstraints=@UniqueConstraint(columnNames="email"))
public class UserAccount {
    @Id @GeneratedValue(strategy=GenerationType.UUID)
    private UUID id;
    @Column(nullable=false, length=200)
    private String email;
    @Column(nullable=false)
    private String passwordHash;
    @Column(nullable=false)
    private String firstName;
    @Column(nullable=false)
    private String lastName;
    @Enumerated(EnumType.STRING) @Column(nullable=false)
    private UserRole role;

    protected UserAccount() {}
    public UserAccount(String email, String passwordHash, String firstName, String lastName, UserRole role) {
        this.email=email; this.passwordHash=passwordHash; this.firstName=firstName;
        this.lastName=lastName; this.role=role;
    }
    public UUID getId() { return id; }
    public String getEmail() { return email; }
    public String getPasswordHash() { return passwordHash; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public UserRole getRole() { return role; }
}
