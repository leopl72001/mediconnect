package com.universidad.mediconnect2.prescriptions;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
public class PrescriptionController {
    private final PrescriptionService service;
    public PrescriptionController(PrescriptionService service) { this.service = service; }

    public record LineRequest(@NotBlank String medication, @NotBlank String dosage,
                              @NotBlank String frequency) {}
    public record CreateRequest(@NotEmpty @Size(max = 20) List<@Valid LineRequest> items) {}
    public record LineView(String medication, String dosage, String frequency,
                           ValidationStatus validationStatus) {
        static LineView from(PrescriptionItem item) {
            return new LineView(item.getMedication(), item.getDosage(),
                    item.getFrequency(), item.getValidationStatus());
        }
    }
    public record PrescriptionView(UUID id, UUID appointmentId, String createdBy,
                                   Instant createdAt, List<LineView> items) {
        static PrescriptionView from(Prescription prescription) {
            return new PrescriptionView(prescription.getId(),
                    prescription.getAppointment().getId(),
                    prescription.getCreatedBy(), prescription.getCreatedAt(),
                    prescription.getItems().stream().map(LineView::from).toList());
        }
    }

    @PostMapping("/api/v1/appointments/{appointmentId}/prescriptions")
    @ResponseStatus(HttpStatus.CREATED)
    public PrescriptionView create(Authentication auth, @PathVariable UUID appointmentId,
                                   @Valid @RequestBody CreateRequest request) {
        List<PrescriptionService.Line> lines = request.items().stream()
                .map(item -> new PrescriptionService.Line(
                        item.medication(), item.dosage(), item.frequency())).toList();
        return PrescriptionView.from(service.create(auth.getName(), appointmentId, lines));
    }

    @GetMapping("/api/v1/prescriptions/me")
    public List<PrescriptionView> mine(Authentication auth) {
        return service.mine(auth.getName()).stream().map(PrescriptionView::from).toList();
    }
}
