package ar.edu.utn.dds.k3003.presentation.metrics.heartbeat;

import ar.edu.utn.dds.k3003.presentation.metrics.MetricsConfig;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.prometheusmetrics.PrometheusMeterRegistry;

public class HeartbeatMonitor {

    private long lastHeartbeatTime;

    public HeartbeatMonitor(MetricsConfig metricsConfig) {
        PrometheusMeterRegistry registry = metricsConfig.getRegistry();

        lastHeartbeatTime = System.currentTimeMillis();
        Gauge.builder("app_heartbeat", this, HeartbeatMonitor::getLastHeartbeatTime)
                .description("Indicates the last time the application was alive")
                .register(registry);
    }

    public long getLastHeartbeatTime() {
        return lastHeartbeatTime;
    }

    public void updateHeartbeat() {
        lastHeartbeatTime = System.currentTimeMillis();
    }
}
