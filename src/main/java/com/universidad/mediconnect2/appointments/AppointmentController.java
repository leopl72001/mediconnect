package com.universidad.mediconnect2.appointments;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/appointments")
public class AppointmentController {
    private final AppointmentService service;
    public AppointmentController(AppointmentService service) { this.service=service; }

    public record CreateRequest(@NotNull UUID doctorId,@NotNull UUID officeId,
                                @NotNull Instant startTime,@NotNull Instant endTime) {}
    public record AppointmentView(UUID id,UUID doctorId,UUID officeId,
                                  Instant startTime,Instant endTime,AppointmentStatus status) {
        static AppointmentView from(Appointment a) {
            return new AppointmentView(a.getId(),a.getDoctor().getId(),a.getOffice().getId(),
                    a.getStartTime(),a.getEndTime(),a.getStatus());
        }
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AppointmentView create(Authentication auth,@Valid @RequestBody CreateRequest request) {
        return AppointmentView.from(service.create(auth.getName(),request.doctorId(),request.officeId(),
                request.startTime(),request.endTime()));
    }

    @GetMapping("/me")
    public List<AppointmentView> mine(Authentication auth) {
        return service.mine(auth.getName()).stream().map(AppointmentView::from).toList();
    }

    @GetMapping("/doctor/me")
    public List<AppointmentView> doctorSchedule(Authentication auth) {
        return service.doctorSchedule(auth.getName()).stream().map(AppointmentView::from).toList();
    }

    @PatchMapping("/{id}/cancel")
    public AppointmentView cancel(Authentication auth,@PathVariable UUID id) {
        return AppointmentView.from(service.cancel(auth.getName(),id));
    }
}
