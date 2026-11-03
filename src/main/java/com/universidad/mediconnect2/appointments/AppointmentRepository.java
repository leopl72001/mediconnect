package com.universidad.mediconnect2.appointments;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppointmentRepository extends JpaRepository<Appointment,UUID> {
    boolean existsByDoctor_IdAndStatusNotAndStartTimeLessThanAndEndTimeGreaterThan(
            UUID doctorId,AppointmentStatus excludedStatus,Instant requestedEnd,Instant requestedStart);
    List<Appointment> findByPatient_EmailIgnoreCaseOrderByStartTimeDesc(String email);
    List<Appointment> findByDoctor_User_EmailIgnoreCaseOrderByStartTimeDesc(String email);
}
