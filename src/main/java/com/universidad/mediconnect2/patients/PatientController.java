package com.universidad.mediconnect2.patients;

import java.time.LocalDate;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/patients")
public class PatientController {
    private final PatientService service;
    public PatientController(PatientService service) { this.service = service; }

    public record UpdateProfileRequest(@Past LocalDate birthDate,
                                       @Size(max = 30) String phone,
                                       @Size(max = 500) String address) {}

    @GetMapping("/me")
    public PatientService.PatientView mine(Authentication auth) {
        return service.mine(auth.getName());
    }

    @PutMapping("/me")
    public PatientService.PatientView update(Authentication auth,
            @Valid @RequestBody UpdateProfileRequest request) {
        return service.update(auth.getName(), request.birthDate(), request.phone(), request.address());
    }
}
