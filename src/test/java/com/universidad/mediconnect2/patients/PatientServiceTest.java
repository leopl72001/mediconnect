package com.universidad.mediconnect2.patients;

import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.universidad.mediconnect2.auth.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PatientServiceTest {
    @Mock UserAccountRepository users;
    @Mock PatientProfileRepository profiles;

    @Test
    void patientReadsOnlyOwnProfileByAuthenticatedEmail() {
        UUID id = UUID.randomUUID();
        UserAccount user = mock(UserAccount.class);
        when(users.findByEmailIgnoreCase("patient@example.com")).thenReturn(Optional.of(user));
        when(user.getId()).thenReturn(id);
        when(user.getEmail()).thenReturn("patient@example.com");
        when(user.getFirstName()).thenReturn("Ana");
        when(user.getLastName()).thenReturn("Lopez");
        when(profiles.findById(id)).thenReturn(Optional.empty());

        PatientService service = new PatientService(users, profiles);
        PatientService.PatientView view = service.mine("patient@example.com");
        assertEquals(id.toString(), view.id());
        assertEquals("patient@example.com", view.email());
        assertNull(view.birthDate());
    }
}
