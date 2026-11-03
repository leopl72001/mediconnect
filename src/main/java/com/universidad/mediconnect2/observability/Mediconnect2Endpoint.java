package com.universidad.mediconnect2.observability;

import java.util.Map;
import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.stereotype.Component;

@Component
@Endpoint(id="mediconnect2")
public class Mediconnect2Endpoint {
    @ReadOperation
    public Map<String,Object> info() {
        return Map.of("application","MediConnect 2",
                "virtualThread",Thread.currentThread().isVirtual(),
                "phase","1-core");
    }
}
