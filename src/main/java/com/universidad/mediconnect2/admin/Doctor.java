package com.universidad.mediconnect2.admin;

import java.util.UUID;
import jakarta.persistence.*;
import com.universidad.mediconnect2.auth.UserAccount;
import com.universidad.mediconnect2.catalog.Specialty;

@Entity
@Table(name="doctors")
public class Doctor {
    @Id @GeneratedValue(strategy=GenerationType.UUID)
    private UUID id;
    @OneToOne(optional=false,fetch=FetchType.LAZY)
    @JoinColumn(name="user_id",nullable=false,unique=true)
    private UserAccount user;
    @ManyToOne(optional=false,fetch=FetchType.LAZY)
    @JoinColumn(name="specialty_id",nullable=false)
    private Specialty specialty;
    @Column(nullable=false,unique=true)
    private String medicalLicense;
    protected Doctor() {}
    public Doctor(UserAccount user, Specialty specialty, String medicalLicense) {
        this.user=user; this.specialty=specialty; this.medicalLicense=medicalLicense;
    }
    public UUID getId() { return id; }
    public UserAccount getUser() { return user; }
    public Specialty getSpecialty() { return specialty; }
    public String getMedicalLicense() { return medicalLicense; }
}
