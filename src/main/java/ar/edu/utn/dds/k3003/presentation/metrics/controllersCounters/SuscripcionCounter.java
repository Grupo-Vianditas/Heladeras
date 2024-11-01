package ar.edu.utn.dds.k3003.presentation.metrics.controllersCounters;

import io.micrometer.core.instrument.Counter;

import ar.edu.utn.dds.k3003.presentation.metrics.MetricsConfig;
import io.micrometer.core.instrument.Counter;
import io.micrometer.prometheusmetrics.PrometheusMeterRegistry;

public class SuscripcionCounter {
    // Exitosos
    private final Counter postSucessfulSuscripcionCounter;

    // Fallidos
    private final Counter postFailedSuscripcionCounter;

    public SuscripcionCounter(MetricsConfig metricsConfig) {
        PrometheusMeterRegistry registry = metricsConfig.getRegistry();

        // Contadores para el endpoint POST /heladeras
        postSucessfulSuscripcionCounter = Counter.builder("requests_post_suscripciones")
                .tag("endpoint", "/suscribir")
                .tag("status", "successful")
                .tag("method", "POST")
                .description("Total successful POST requests to /suscribir")
                .register(registry);

        postFailedSuscripcionCounter = Counter.builder("requests_post_suscripciones")
                .tag("endpoint", "/suscribir")
                .tag("status", "failed")
                .tag("method", "POST")
                .description("Total failed POST requests to /suscribir")
                .register(registry);

    }

    public void incrementSucessfulPostCounter() {
        postSucessfulSuscripcionCounter.increment();
    }

    public void incrementFailedPostCounter() {
        postFailedSuscripcionCounter.increment();
    }

}