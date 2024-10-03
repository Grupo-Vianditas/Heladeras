package ar.edu.utn.dds.k3003.presentation.metrics.controllersCounters;

import ar.edu.utn.dds.k3003.presentation.metrics.MetricsConfig;
import io.micrometer.core.instrument.Counter;
import io.micrometer.prometheusmetrics.PrometheusMeterRegistry;

public class ViandasCounter {

    // Contadores generales para GET y POST
    // Separados por exitosos y fallidos

    // Exitosos
    private final Counter postSuccessfulViandasCounter;
    private final Counter getSuccessfulViandasCounter;

    // Fallidos
    private final Counter postFailedViandasCounter;
    private final Counter getFailedViandasCounter;

    public ViandasCounter(MetricsConfig metricsConfig) {
        PrometheusMeterRegistry registry = metricsConfig.getRegistry();

        // Contador para el endpoint POST /depositos (exitoso y fallido)
        postSuccessfulViandasCounter = Counter.builder("requests_post_viandas")
                .tag("endpoint", "/depositos")
                .tag("status", "successful")
                .tag("method", "POST")
                .description("Total successful POST requests to /depositos")
                .register(registry);

        postFailedViandasCounter = Counter.builder("requests_post_viandas")
                .tag("endpoint", "/depositos")
                .tag("status", "failed")
                .tag("method", "POST")
                .description("Total failed POST requests to /depositos")
                .register(registry);

        // Contador para el endpoint GET /retiros (exitoso y fallido)
        getSuccessfulViandasCounter = Counter.builder("requests_get_viandas")
                .tag("endpoint", "/retiros")
                .tag("status", "successful")
                .tag("method", "GET")
                .description("Total successful GET requests to /retiros")
                .register(registry);

        getFailedViandasCounter = Counter.builder("requests_get_viandas")
                .tag("endpoint", "/retiros")
                .tag("status", "failed")
                .tag("method", "GET")
                .description("Total failed GET requests to /retiros")
                .register(registry);
    }

    // Métodos para incrementar los contadores de POST
    public void incrementSuccessfulPostCounter() {
        postSuccessfulViandasCounter.increment();
    }

    public void incrementFailedPostCounter() {
        postFailedViandasCounter.increment();
    }

    // Métodos para incrementar los contadores de GET
    public void incrementSuccessfulGetCounter() {
        getSuccessfulViandasCounter.increment();
    }

    public void incrementFailedGetCounter() {
        getFailedViandasCounter.increment();
    }
}
