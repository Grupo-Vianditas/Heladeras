package ar.edu.utn.dds.k3003.service;

import ar.edu.utn.dds.k3003.presentation.metrics.controllersCounters.MetricsFactory;
import ar.edu.utn.dds.k3003.presentation.metrics.controllersCounters.counterMetrics.Metrics;

public class MetricsService {

    private final MetricsFactory metricsFactory;

    public MetricsService(MetricsFactory metricsFactory) {
        this.metricsFactory = metricsFactory;
    }

    public void increment(String endpoint, String method, String status) {
        Metrics metrics = metricsFactory.createMetrics(endpoint, method, status);
        if ("successful".equals(status)) {
            metrics.incrementSuccessful();
        } else {
            metrics.incrementFailed();
        }
    }
}