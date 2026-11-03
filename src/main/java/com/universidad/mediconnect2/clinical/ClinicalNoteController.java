package com.universidad.mediconnect2.clinical;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@Profile({"prod","docker"})
public class ClinicalNoteController {
    private final ClinicalNoteService service;
    public ClinicalNoteController(ClinicalNoteService service) { this.service = service; }

    public record NoteRequest(@NotBlank @Size(max = 10000) String content) {}
    public record NoteView(String id, String appointmentId, String content,
                           String createdBy, Instant createdAt) {
        static NoteView from(ClinicalNoteDocument note) {
            return new NoteView(note.getId(), note.getAppointmentId(),
                    note.getContent(), note.getCreatedBy(), note.getCreatedAt());
        }
    }

    @PostMapping("/api/v1/appointments/{appointmentId}/clinical-notes")
    @ResponseStatus(HttpStatus.CREATED)
    public NoteView add(Authentication auth, @PathVariable UUID appointmentId,
                        @Valid @RequestBody NoteRequest request) {
        return NoteView.from(service.add(auth.getName(), appointmentId, request.content()));
    }

    @GetMapping("/api/v1/clinical-notes/me")
    public List<NoteView> mine(Authentication auth) {
        return service.mine(auth.getName()).stream().map(NoteView::from).toList();
    }
}
