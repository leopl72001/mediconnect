package com.universidad.mediconnect2.clinical;

import java.time.Clock;
import java.util.List;
import java.util.UUID;
import org.springframework.context.annotation.Profile;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.universidad.mediconnect2.appointments.*;
import com.universidad.mediconnect2.auth.*;
import com.universidad.mediconnect2.shared.NotFoundException;

@Service
@Profile("prod")
public class ClinicalNoteService {
    private final ClinicalNoteRepository notes;
    private final AppointmentRepository appointments;
    private final UserAccountRepository users;
    private final Clock clock;

    public ClinicalNoteService(ClinicalNoteRepository notes, AppointmentRepository appointments,
                               UserAccountRepository users, Clock clock) {
        this.notes = notes;
        this.appointments = appointments;
        this.users = users;
        this.clock = clock;
    }

    @Transactional(readOnly = true)
    @PreAuthorize("hasRole('DOCTOR')")
    public ClinicalNoteDocument add(String doctorEmail, UUID appointmentId, String content) {
        Appointment appointment = appointments.findById(appointmentId)
                .orElseThrow(() -> new NotFoundException("Cita no encontrada."));
        if (!appointment.getDoctor().getUser().getEmail().equalsIgnoreCase(doctorEmail)) {
            throw new AccessDeniedException("Solo el medico asignado puede escribir notas.");
        }
        if (appointment.getStatus() == AppointmentStatus.CANCELLED) {
            throw new IllegalArgumentException("No se escriben notas en citas canceladas.");
        }
        return notes.save(new ClinicalNoteDocument(
                appointment.getId().toString(),
                appointment.getPatient().getId().toString(),
                appointment.getDoctor().getId().toString(),
                content.trim(), doctorEmail, clock.instant()));
    }

    @Transactional(readOnly = true)
    @PreAuthorize("hasRole('PATIENT')")
    public List<ClinicalNoteDocument> mine(String patientEmail) {
        UserAccount patient = users.findByEmailIgnoreCase(patientEmail)
                .orElseThrow(() -> new NotFoundException("Paciente no encontrado."));
        return notes.findByPatientIdOrderByCreatedAtDesc(patient.getId().toString());
    }
}
