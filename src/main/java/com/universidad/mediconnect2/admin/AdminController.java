package com.universidad.mediconnect2.admin;

import java.util.Locale;
import java.util.UUID;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import com.universidad.mediconnect2.auth.*;
import com.universidad.mediconnect2.catalog.*;
import com.universidad.mediconnect2.shared.*;

@RestController
@RequestMapping("/api/v1/admin")
public class AdminController {
    private final OfficeRepository offices;
    private final DoctorRepository doctors;
    private final SpecialtyRepository specialties;
    private final UserAccountRepository users;
    private final PasswordEncoder passwords;

    public AdminController(OfficeRepository offices, DoctorRepository doctors,
            SpecialtyRepository specialties, UserAccountRepository users, PasswordEncoder passwords) {
        this.offices=offices; this.doctors=doctors; this.specialties=specialties;
        this.users=users; this.passwords=passwords;
    }

    public record OfficeRequest(@NotBlank String name,@NotBlank String address,
                                @NotBlank String roomNumber,boolean active) {}
    public record DoctorRequest(@NotBlank @Email String email,
                                @NotBlank @Size(min=8) String temporaryPassword,
                                @NotBlank String firstName,@NotBlank String lastName,
                                @NotBlank String medicalLicense,@NotNull UUID specialtyId) {}
    public record CreatedResponse(UUID id) {}

    @PostMapping("/offices")
    @ResponseStatus(HttpStatus.CREATED)
    public CreatedResponse createOffice(@Valid @RequestBody OfficeRequest request) {
        Office office=offices.save(new Office(request.name().trim(), request.address().trim(),
                request.roomNumber().trim(), request.active()));
        return new CreatedResponse(office.getId());
    }

    @PostMapping("/doctors")
    @ResponseStatus(HttpStatus.CREATED)
    @Transactional
    public CreatedResponse createDoctor(@Valid @RequestBody DoctorRequest request) {
        String email=request.email().trim().toLowerCase(Locale.ROOT);
        if (users.existsByEmailIgnoreCase(email)) throw new ConflictException("El correo ya existe.");
        if (doctors.existsByMedicalLicenseIgnoreCase(request.medicalLicense()))
            throw new ConflictException("El registro medico ya existe.");
        Specialty specialty=specialties.findById(request.specialtyId())
                .orElseThrow(() -> new NotFoundException("Especialidad no encontrada."));
        UserAccount user=users.save(new UserAccount(email,passwords.encode(request.temporaryPassword()),
                request.firstName().trim(),request.lastName().trim(),UserRole.ROLE_DOCTOR));
        Doctor doctor=doctors.save(new Doctor(user,specialty,request.medicalLicense().trim()));
        return new CreatedResponse(doctor.getId());
    }
}
