package com.universidad.mediconnect2.appointments;

import java.time.Duration;
import java.time.Instant;
import com.universidad.mediconnect2.shared.LateCancellationException;

public final class AppointmentPolicy {
    private AppointmentPolicy() {}

    public static void validateWindow(Instant start,Instant end,Instant now) {
        if (start==null || end==null || !end.isAfter(start))
            throw new IllegalArgumentException("La hora final debe ser posterior a la inicial.");
        if (!start.isAfter(now))
            throw new IllegalArgumentException("La cita debe programarse en el futuro.");
    }

    public static void requireCancellable(Instant start,Instant now) {
        if (Duration.between(now,start).compareTo(Duration.ofHours(24))<=0)
            throw new LateCancellationException("Solo se permite cancelar con mas de 24 horas de anticipacion.");
    }
}
