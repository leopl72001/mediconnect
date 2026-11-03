package com.universidad.mediconnect2.prescriptions;

import java.time.Clock;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;
import com.universidad.mediconnect2.appointments.*;
import com.universidad.mediconnect2.admin.Doctor;
import com.universidad.mediconnect2.auth.UserAccount;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PrescriptionServiceTest {
    @Mock PrescriptionRepository prescriptions;
    @Mock AppointmentRepository appointments;
    @Mock OpenFdaService openFda;
    @Mock Clock clock;

    @Test
    void otherDoctorCannotPrescribe() {
        UUID id = UUID.randomUUID();
        Appointment appointment = mock(Appointment.class);
        Doctor doctor = mock(Doctor.class);
        UserAccount doctorUser = mock(UserAccount.class);
        when(appointments.findById(id)).thenReturn(Optional.of(appointment));
        when(appointment.getDoctor()).thenReturn(doctor);
        when(doctor.getUser()).thenReturn(doctorUser);
        when(doctorUser.getEmail()).thenReturn("assigned@example.com");

        PrescriptionService service = new PrescriptionService(prescriptions, appointments, openFda, clock);
        assertThrows(AccessDeniedException.class, () -> service.create(
                "other@example.com", id, List.of(new PrescriptionService.Line("Drug", "10 mg", "daily"))));
        verifyNoInteractions(openFda, prescriptions);
    }

    @Test
    void cancelledAppointmentCannotBePrescribed() {
        UUID id = UUID.randomUUID();
        Appointment appointment = mock(Appointment.class);
        Doctor doctor = mock(Doctor.class);
        UserAccount doctorUser = mock(UserAccount.class);
        when(appointments.findById(id)).thenReturn(Optional.of(appointment));
        when(appointment.getDoctor()).thenReturn(doctor);
        when(doctor.getUser()).thenReturn(doctorUser);
        when(doctorUser.getEmail()).thenReturn("assigned@example.com");
        when(appointment.getStatus()).thenReturn(AppointmentStatus.CANCELLED);

        PrescriptionService service = new PrescriptionService(prescriptions, appointments, openFda, clock);
        assertThrows(IllegalArgumentException.class, () -> service.create(
                "assigned@example.com", id, List.of(new PrescriptionService.Line("Drug", "10 mg", "daily"))));
        verifyNoInteractions(openFda, prescriptions);
    }
}
