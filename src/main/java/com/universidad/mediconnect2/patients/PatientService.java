package com.universidad.mediconnect2.patients;

import java.time.LocalDate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.universidad.mediconnect2.auth.*;
import com.universidad.mediconnect2.shared.NotFoundException;

@Service
public class PatientService {
    private final UserAccountRepository users;
    private final PatientProfileRepository profiles;
    public PatientService(UserAccountRepository users, PatientProfileRepository profiles) {
        this.users = users;
        this.profiles = profiles;
    }

    public record PatientView(String id, String email, String firstName, String lastName,
                              LocalDate birthDate, String phone, String address) {}

    @Transactional(readOnly = true)
    @PreAuthorize("hasRole('PATIENT')")
    public PatientView mine(String email) {
        UserAccount user = user(email);
        PatientProfile profile = profiles.findById(user.getId()).orElse(null);
        return view(user, profile);
    }

    @Transactional
    @PreAuthorize("hasRole('PATIENT')")
    public PatientView update(String email, LocalDate birthDate, String phone, String address) {
        UserAccount user = user(email);
        PatientProfile profile = profiles.findById(user.getId())
                .orElseGet(() -> new PatientProfile(user));
        profile.update(birthDate, phone, address);
        return view(user, profiles.save(profile));
    }

    private UserAccount user(String email) {
        return users.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new NotFoundException("Paciente no encontrado."));
    }
    private PatientView view(UserAccount user, PatientProfile profile) {
        return new PatientView(user.getId().toString(), user.getEmail(),
                user.getFirstName(), user.getLastName(),
                profile == null ? null : profile.getBirthDate(),
                profile == null ? null : profile.getPhone(),
                profile == null ? null : profile.getAddress());
    }
}
