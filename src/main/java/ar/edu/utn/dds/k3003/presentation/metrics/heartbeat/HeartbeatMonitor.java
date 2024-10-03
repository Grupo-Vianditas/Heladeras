package ar.edu.utn.dds.k3003.presentation.metrics.heartbeat;

import ar.edu.utn.dds.k3003.presentation.metrics.MetricsConfig;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.prometheusmetrics.PrometheusMeterRegistry;

public class HeartbeatMonitor {

    private static int isalive;

    public HeartbeatMonitor(MetricsConfig metricsConfig) {
        PrometheusMeterRegistry registry = metricsConfig.getRegistry();

        Gauge.builder("app_heartbeat", this, HeartbeatMonitor::isalive)
                .description("Indicates if the application is alive")
                .register(registry);
    }

    public long isalive() {
        return isalive;
    }

    public void setAlive(int status) {
        isalive = status;
    }

}
