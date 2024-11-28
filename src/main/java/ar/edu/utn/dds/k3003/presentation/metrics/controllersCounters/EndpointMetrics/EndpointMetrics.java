package ar.edu.utn.dds.k3003.presentation.metrics.controllersCounters.EndpointMetrics;

import ar.edu.utn.dds.k3003.presentation.metrics.controllersCounters.counterMetrics.Metrics;
import io.micrometer.core.instrument.Counter;
import io.micrometer.prometheusmetrics.PrometheusMeterRegistry;

public class EndpointMetrics implements Metrics {

    private final Counter successfulCounter;
    private final Counter failedCounter;

    public EndpointMetrics(PrometheusMeterRegistry registry, String endpoint, String method, String status) {
        successfulCounter = Counter.builder("requests_" + method.toLowerCase() + "_" + endpoint.replace("/", "_"))
                .tag("endpoint", endpoint)
                .tag("method", method)
                .tag("status", status)
                .description("Total " + status + " " + method + " requests to " + endpoint)
                .register(registry);

        failedCounter = Counter.builder("requests_failed_" + method.toLowerCase() + "_" + endpoint.replace("/", "_"))
                .tag("endpoint", endpoint)
                .tag("method", method)
                .tag("status", "failed")
                .description("Total failed " + method + " requests to " + endpoint)
                .register(registry);
    }

    @Override
    public void incrementSuccessful() {
        successfulCounter.increment();
    }

    @Override
    public void incrementFailed() {
        failedCounter.increment();
    }
}
