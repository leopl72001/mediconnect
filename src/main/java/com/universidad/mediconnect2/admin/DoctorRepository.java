package com.universidad.mediconnect2.admin;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface DoctorRepository extends JpaRepository<Doctor,UUID> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select d from Doctor d where d.id = :id")
    Optional<Doctor> findLockedById(@Param("id") UUID id);
    Optional<Doctor> findByUser_EmailIgnoreCase(String email);
    List<Doctor> findBySpecialty_Id(UUID specialtyId);
    boolean existsByMedicalLicenseIgnoreCase(String medicalLicense);
}
