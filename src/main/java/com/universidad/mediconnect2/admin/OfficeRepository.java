package com.universidad.mediconnect2.admin;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
public interface OfficeRepository extends JpaRepository<Office, UUID> {}
