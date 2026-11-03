package com.universidad.mediconnect2.prescriptions;

import java.time.Clock;
import java.util.List;
import java.util.UUID;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.universidad.mediconnect2.appointments.*;
import com.universidad.mediconnect2.shared.NotFoundException;

@Service
public class PrescriptionService {
    private final PrescriptionRepository prescriptions;
    private final AppointmentRepository appointments;
    private final OpenFdaService openFda;
    private final Clock clock;

    public PrescriptionService(PrescriptionRepository prescriptions, AppointmentRepository appointments,
                               OpenFdaService openFda, Clock clock) {
        this.prescriptions = prescriptions;
        this.appointments = appointments;
        this.openFda = openFda;
        this.clock = clock;
    }

    public record Line(String medication, String dosage, String frequency) {}

    @Transactional
    @PreAuthorize("hasRole('DOCTOR')")
    public Prescription create(String doctorEmail, UUID appointmentId, List<Line> lines) {
        Appointment appointment = appointments.findById(appointmentId)
                .orElseThrow(() -> new NotFoundException("Cita no encontrada."));
        if (!appointment.getDoctor().getUser().getEmail().equalsIgnoreCase(doctorEmail)) {
            throw new AccessDeniedException("Solo el medico asignado puede recetar.");
        }
        if (appointment.getStatus() == AppointmentStatus.CANCELLED) {
            throw new IllegalArgumentException("No se emiten recetas para citas canceladas.");
        }
        List<PrescriptionItem> items = lines.stream().map(line -> new PrescriptionItem(
                line.medication().trim(), line.dosage().trim(), line.frequency().trim(),
                openFda.validate(line.medication()))).toList();
        return prescriptions.save(new Prescription(appointment, doctorEmail, clock.instant(), items));
    }

    @Transactional(readOnly = true)
    @PreAuthorize("hasRole('PATIENT')")
    public List<Prescription> mine(String patientEmail) {
        return prescriptions.findByAppointment_Patient_EmailIgnoreCaseOrderByCreatedAtDesc(patientEmail);
    }
}
