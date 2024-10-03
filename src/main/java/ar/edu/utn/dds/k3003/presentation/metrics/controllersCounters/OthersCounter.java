package ar.edu.utn.dds.k3003.presentation.metrics.controllersCounters;

import ar.edu.utn.dds.k3003.presentation.metrics.MetricsConfig;
import io.micrometer.core.instrument.Counter;
import io.micrometer.prometheusmetrics.PrometheusMeterRegistry;

public class OthersCounter {

    // Contadores generales para los endpoints específicos
    // Separados por exitosos y fallidos

    // Exitosos
    private final Counter postSuccessfulMockerCounter;
    private final Counter postSuccessfulCleanerCounter;

    // Fallidos
    private final Counter postFailedMockerCounter;
    private final Counter postFailedCleanerCounter;

    public OthersCounter(MetricsConfig metricsConfig) {
        PrometheusMeterRegistry registry = metricsConfig.getRegistry();

        // Contador para el endpoint POST /mock (exitoso y fallido)
        postSuccessfulMockerCounter = Counter.builder("requests_post_mock")
                .tag("endpoint", "/mock")
                .tag("status", "successful")
                .tag("method", "POST")
                .description("Total successful POST requests to /mock")
                .register(registry);

        postFailedMockerCounter = Counter.builder("requests_post_mock")
                .tag("endpoint", "/mock")
                .tag("status", "failed")
                .tag("method", "POST")
                .description("Total failed POST requests to /mock")
                .register(registry);

        // Contador para el endpoint POST /clear (exitoso y fallido)
        postSuccessfulCleanerCounter = Counter.builder("requests_post_clear")
                .tag("endpoint", "/clear")
                .tag("status", "successful")
                .tag("method", "POST")
                .description("Total successful POST requests to /clear")
                .register(registry);

        postFailedCleanerCounter = Counter.builder("requests_post_clear")
                .tag("endpoint", "/clear")
                .tag("status", "failed")
                .tag("method", "POST")
                .description("Total failed POST requests to /clear")
                .register(registry);
    }

    // Métodos para incrementar los contadores de POST /mock
    public void incrementSuccessfulMockCounter() {
        postSuccessfulMockerCounter.increment();
    }

    public void incrementFailedMockCounter() {
        postFailedMockerCounter.increment();
    }

    // Métodos para incrementar los contadores de POST /clear
    public void incrementSuccessfulClearCounter() {
        postSuccessfulCleanerCounter.increment();
    }

    public void incrementFailedClearCounter() {
        postFailedCleanerCounter.increment();
    }
}
