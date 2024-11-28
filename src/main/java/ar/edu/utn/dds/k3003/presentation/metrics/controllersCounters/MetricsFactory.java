package ar.edu.utn.dds.k3003.presentation.metrics.controllersCounters;

import ar.edu.utn.dds.k3003.presentation.metrics.controllersCounters.EndpointMetrics.EndpointMetrics;
import ar.edu.utn.dds.k3003.presentation.metrics.controllersCounters.counterMetrics.Metrics;
import io.micrometer.prometheusmetrics.PrometheusMeterRegistry;

public class MetricsFactory {

    private final PrometheusMeterRegistry registry;

    public MetricsFactory(PrometheusMeterRegistry registry) {
        this.registry = registry;
    }

    public Metrics createMetrics(String endpoint, String method, String status) {
        return new EndpointMetrics(registry, endpoint, method, status);
    }
}

