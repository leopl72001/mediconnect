package com.universidad.mediconnect2.prescriptions;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PrescriptionRepository extends JpaRepository<Prescription, UUID> {
    List<Prescription> findByAppointment_Patient_EmailIgnoreCaseOrderByCreatedAtDesc(String email);
}
