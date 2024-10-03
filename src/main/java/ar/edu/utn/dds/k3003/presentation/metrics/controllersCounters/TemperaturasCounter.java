package ar.edu.utn.dds.k3003.presentation.metrics.controllersCounters;

import ar.edu.utn.dds.k3003.presentation.metrics.MetricsConfig;
import io.micrometer.core.instrument.Counter;
import io.micrometer.prometheusmetrics.PrometheusMeterRegistry;

public class TemperaturasCounter {

    // Contadores generales para GET y POST
    // Separados por exitosos y fallidos

    // Exitosos
    private final Counter postSuccessfulTemperaturasCounter;
    private final Counter getSuccessfulTemperaturasCounter;

    // Fallidos
    private final Counter postFailedTemperaturasCounter;
    private final Counter getFailedTemperaturasCounter;

    public TemperaturasCounter(MetricsConfig metricsConfig) {
        PrometheusMeterRegistry registry = metricsConfig.getRegistry();

        // Contador para el endpoint POST /temperaturas (exitoso y fallido)
        postSuccessfulTemperaturasCounter = Counter.builder("requests_post_temperaturas")
                .tag("endpoint", "/temperaturas")
                .tag("status", "successful")
                .tag("method", "POST")
                .description("Total successful POST requests to /temperaturas")
                .register(registry);

        postFailedTemperaturasCounter = Counter.builder("requests_post_temperaturas")
                .tag("endpoint", "/temperaturas")
                .tag("status", "failed")
                .tag("method", "POST")
                .description("Total failed POST requests to /temperaturas")
                .register(registry);

        // Contador para el endpoint GET /heladeras/{heladeraId}/temperaturas (exitoso y fallido)
        getSuccessfulTemperaturasCounter = Counter.builder("requests_get_temperaturas")
                .tag("endpoint", "/heladeras/{heladeraId}/temperaturas")
                .tag("status", "successful")
                .tag("method", "GET")
                .description("Total successful GET requests to /heladeras/{heladeraId}/temperaturas")
                .register(registry);

        getFailedTemperaturasCounter = Counter.builder("requests_get_temperaturas")
                .tag("endpoint", "/heladeras/{heladeraId}/temperaturas")
                .tag("status", "failed")
                .tag("method", "GET")
                .description("Total failed GET requests to /heladeras/{heladeraId}/temperaturas")
                .register(registry);
    }

    // Métodos para incrementar los contadores de POST
    public void incrementSuccessfulPostCounter() {
        postSuccessfulTemperaturasCounter.increment();
    }

    public void incrementFailedPostCounter() {
        postFailedTemperaturasCounter.increment();
    }

    // Métodos para incrementar los contadores de GET
    public void incrementSuccessfulGetCounter() {
        getSuccessfulTemperaturasCounter.increment();
    }

    public void incrementFailedGetCounter() {
        getFailedTemperaturasCounter.increment();
    }
}
