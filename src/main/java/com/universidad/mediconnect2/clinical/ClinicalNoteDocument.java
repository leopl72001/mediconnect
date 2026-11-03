package com.universidad.mediconnect2.clinical;

import java.time.Instant;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "clinical_notes")
public class ClinicalNoteDocument {
    @Id private String id;
    @Indexed private String appointmentId;
    @Indexed private String patientId;
    private String doctorId;
    private String content;
    private String createdBy;
    private Instant createdAt;

    protected ClinicalNoteDocument() {}

    public ClinicalNoteDocument(String appointmentId, String patientId, String doctorId,
                                String content, String createdBy, Instant createdAt) {
        this.appointmentId = appointmentId;
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.content = content;
        this.createdBy = createdBy;
        this.createdAt = createdAt;
    }
    public String getId() { return id; }
    public String getAppointmentId() { return appointmentId; }
    public String getPatientId() { return patientId; }
    public String getDoctorId() { return doctorId; }
    public String getContent() { return content; }
    public String getCreatedBy() { return createdBy; }
    public Instant getCreatedAt() { return createdAt; }
}
