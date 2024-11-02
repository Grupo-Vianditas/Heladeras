package ar.edu.utn.dds.k3003.presentation.metrics.controllersCounters;

import ar.edu.utn.dds.k3003.presentation.metrics.MetricsConfig;
import io.micrometer.core.instrument.Counter;
import io.micrometer.prometheusmetrics.PrometheusMeterRegistry;

public class IncidentesCounter {

    private final Counter postSucessfulIncidentesCounter;
    private final Counter postFailedIncidentesCounter;

    public IncidentesCounter(MetricsConfig metricsConfig) {
        PrometheusMeterRegistry registry = metricsConfig.getRegistry();

        postSucessfulIncidentesCounter = Counter.builder("requests_post_incidentes")
                .tag("endpoint", "/incidentes")
                .tag("status","successful")
                .tag("method", "POST")
                .description("Total successful POST requests to /incidentes")
                .register(registry);

        postFailedIncidentesCounter = Counter.builder("requests_post_incidentes")
                .tag("endpoint", "/incidentes")
                .tag("status","failed")
                .tag("method", "POST")
                .description("Total failed POST requests to /incidentes")
                .register(registry);

    }

    public void incrementSucessfulPostCounter() {
        postSucessfulIncidentesCounter.increment();
    }

    public void incrementFailedPostCounter() {
        postFailedIncidentesCounter.increment();
    }
}
