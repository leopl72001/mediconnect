package com.universidad.mediconnect2.appointments;

import java.time.Instant;
import java.util.UUID;
import jakarta.persistence.*;
import com.universidad.mediconnect2.admin.Doctor;
import com.universidad.mediconnect2.admin.Office;
import com.universidad.mediconnect2.auth.UserAccount;

@Entity
@Table(name="appointments", indexes=@Index(name="idx_appointments_doctor_time",columnList="doctor_id,start_time,end_time"))
public class Appointment {
    @Id @GeneratedValue(strategy=GenerationType.UUID)
    private UUID id;
    @ManyToOne(optional=false,fetch=FetchType.LAZY) @JoinColumn(name="patient_id",nullable=false)
    private UserAccount patient;
    @ManyToOne(optional=false,fetch=FetchType.LAZY) @JoinColumn(name="doctor_id",nullable=false)
    private Doctor doctor;
    @ManyToOne(optional=false,fetch=FetchType.LAZY) @JoinColumn(name="office_id",nullable=false)
    private Office office;
    @Column(name="start_time",nullable=false)
    private Instant startTime;
    @Column(name="end_time",nullable=false)
    private Instant endTime;
    @Enumerated(EnumType.STRING) @Column(nullable=false)
    private AppointmentStatus status;
    protected Appointment() {}
    public Appointment(UserAccount patient,Doctor doctor,Office office,Instant startTime,Instant endTime) {
        this.patient=patient; this.doctor=doctor; this.office=office;
        this.startTime=startTime; this.endTime=endTime; this.status=AppointmentStatus.SCHEDULED;
    }
    public UUID getId() { return id; }
    public UserAccount getPatient() { return patient; }
    public Doctor getDoctor() { return doctor; }
    public Office getOffice() { return office; }
    public Instant getStartTime() { return startTime; }
    public Instant getEndTime() { return endTime; }
    public AppointmentStatus getStatus() { return status; }
    public void cancel() { this.status=AppointmentStatus.CANCELLED; }
}
