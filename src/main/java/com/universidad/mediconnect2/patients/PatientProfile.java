package com.universidad.mediconnect2.patients;

import java.time.LocalDate;
import java.util.UUID;
import jakarta.persistence.*;
import com.universidad.mediconnect2.auth.UserAccount;

@Entity
@Table(name = "patient_profiles")
public class PatientProfile {
    @Id private UUID userId;
    @MapsId
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id")
    private UserAccount user;
    private LocalDate birthDate;
    @Column(length = 30) private String phone;
    @Column(length = 500) private String address;

    protected PatientProfile() {}
    public PatientProfile(UserAccount user) { this.user = user; }
    public void update(LocalDate birthDate, String phone, String address) {
        this.birthDate = birthDate;
        this.phone = phone == null ? null : phone.trim();
        this.address = address == null ? null : address.trim();
    }
    public UUID getUserId() { return userId; }
    public LocalDate getBirthDate() { return birthDate; }
    public String getPhone() { return phone; }
    public String getAddress() { return address; }
}
