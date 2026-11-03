package com.universidad.mediconnect2.clinical;

import java.time.Clock;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;
import com.universidad.mediconnect2.appointments.*;
import com.universidad.mediconnect2.admin.Doctor;
import com.universidad.mediconnect2.auth.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClinicalNoteServiceTest {
    @Mock ClinicalNoteRepository notes;
    @Mock AppointmentRepository appointments;
    @Mock UserAccountRepository users;
    @Mock Clock clock;

    @Test
    void otherDoctorCannotWriteClinicalNote() {
        UUID id = UUID.randomUUID();
        Appointment appointment = mock(Appointment.class);
        Doctor doctor = mock(Doctor.class);
        UserAccount doctorUser = mock(UserAccount.class);
        when(appointments.findById(id)).thenReturn(Optional.of(appointment));
        when(appointment.getDoctor()).thenReturn(doctor);
        when(doctor.getUser()).thenReturn(doctorUser);
        when(doctorUser.getEmail()).thenReturn("assigned@example.com");

        ClinicalNoteService service = new ClinicalNoteService(notes, appointments, users, clock);
        assertThrows(AccessDeniedException.class,
                () -> service.add("other@example.com", id, "Nota de prueba"));
        verifyNoInteractions(notes);
    }
}
