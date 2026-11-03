package com.universidad.mediconnect2.clinical;

import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ClinicalNoteRepository extends MongoRepository<ClinicalNoteDocument, String> {
    List<ClinicalNoteDocument> findByPatientIdOrderByCreatedAtDesc(String patientId);
}
