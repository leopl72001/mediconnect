package com.universidad.mediconnect2.prescriptions;

import jakarta.persistence.*;

@Embeddable
public class PrescriptionItem {
    @Column(nullable = false) private String medication;
    @Column(nullable = false) private String dosage;
    @Column(nullable = false) private String frequency;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false) private ValidationStatus validationStatus;

    protected PrescriptionItem() {}
    public PrescriptionItem(String medication, String dosage, String frequency, ValidationStatus validationStatus) {
        this.medication = medication;
        this.dosage = dosage;
        this.frequency = frequency;
        this.validationStatus = validationStatus;
    }
    public String getMedication() { return medication; }
    public String getDosage() { return dosage; }
    public String getFrequency() { return frequency; }
    public ValidationStatus getValidationStatus() { return validationStatus; }
}
