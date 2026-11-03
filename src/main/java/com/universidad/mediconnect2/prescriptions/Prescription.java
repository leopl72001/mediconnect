package com.universidad.mediconnect2.prescriptions;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import jakarta.persistence.*;
import com.universidad.mediconnect2.appointments.Appointment;

@Entity
@Table(name = "prescriptions")
public class Prescription {
    @Id @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "appointment_id", nullable = false)
    private Appointment appointment;
    @Column(nullable = false) private String createdBy;
    @Column(nullable = false) private Instant createdAt;
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "prescription_items", joinColumns = @JoinColumn(name = "prescription_id"))
    @OrderColumn(name = "line_number")
    private List<PrescriptionItem> items = new ArrayList<>();

    protected Prescription() {}
    public Prescription(Appointment appointment, String createdBy, Instant createdAt, List<PrescriptionItem> items) {
        this.appointment = appointment;
        this.createdBy = createdBy;
        this.createdAt = createdAt;
        this.items = new ArrayList<>(items);
    }
    public UUID getId() { return id; }
    public Appointment getAppointment() { return appointment; }
    public String getCreatedBy() { return createdBy; }
    public Instant getCreatedAt() { return createdAt; }
    public List<PrescriptionItem> getItems() { return List.copyOf(items); }
}
