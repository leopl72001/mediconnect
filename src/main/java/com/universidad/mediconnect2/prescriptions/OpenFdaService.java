package com.universidad.mediconnect2.prescriptions;

import java.time.Duration;
import com.fasterxml.jackson.databind.JsonNode;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class OpenFdaService {
    private final RestClient client;
    private final CircuitBreaker breaker;
    private final MeterRegistry metrics;

    public OpenFdaService(MeterRegistry metrics) {
        this.metrics = metrics;
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(Duration.ofMillis(1500));
        factory.setReadTimeout(Duration.ofMillis(1500));
        this.client = RestClient.builder()
                .baseUrl("https://api.fda.gov")
                .requestFactory(factory)
                .build();
        CircuitBreakerConfig config = CircuitBreakerConfig.custom()
                .failureRateThreshold(50)
                .slowCallRateThreshold(50)
                .slowCallDurationThreshold(Duration.ofMillis(1500))
                .slidingWindowSize(10)
                .minimumNumberOfCalls(5)
                .waitDurationInOpenState(Duration.ofSeconds(10))
                .permittedNumberOfCallsInHalfOpenState(3)
                .build();
        this.breaker = CircuitBreaker.of("openFda", config);
    }

    @Cacheable(cacheNames = "validatedDrugs", key = "#name.toLowerCase()",
            unless = "#result != T(com.universidad.mediconnect2.prescriptions.ValidationStatus).VALIDATED")
    public ValidationStatus validate(String name) {
        try {
            return breaker.executeSupplier(() -> {
                String safeName = name.trim().replace("\"", "").replace("'", "");
                JsonNode json = client.get()
                        .uri(builder -> builder.path("/drug/label.json")
                                .queryParam("search", "openfda.brand_name:\"" + safeName + "\"")
                                .queryParam("limit", 1).build())
                        .retrieve().body(JsonNode.class);
                if (json == null || !json.path("results").isArray() || json.path("results").isEmpty()) {
                    throw new IllegalStateException("OpenFDA no devolvio resultados.");
                }
                return ValidationStatus.VALIDATED;
            });
        } catch (RuntimeException ex) {
            metrics.counter("mediconnect.circuit_breaker_fallbacks").increment();
            return ValidationStatus.MANUAL_VALIDATION_REQUIRED;
        }
    }

    public String circuitBreakerState() { return breaker.getState().name(); }
}
