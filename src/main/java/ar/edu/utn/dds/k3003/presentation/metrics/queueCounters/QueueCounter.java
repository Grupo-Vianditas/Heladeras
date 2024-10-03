package ar.edu.utn.dds.k3003.presentation.metrics.queueCounters;

import ar.edu.utn.dds.k3003.presentation.metrics.MetricsConfig;
import io.micrometer.core.instrument.Counter;
import io.micrometer.prometheusmetrics.PrometheusMeterRegistry;

public class QueueCounter {

    private final Counter validMessageCounter;
    private final Counter invalidMessageCounter;

    public QueueCounter(MetricsConfig metricsConfig) {
        PrometheusMeterRegistry registry = metricsConfig.getRegistry();

        validMessageCounter = Counter.builder("messages_valid_received")
                .tag("status", "valid")
                .description("Total valid messages received from the queue")
                .register(registry);

        invalidMessageCounter = Counter.builder("messages_invalid_received")
                .tag("status", "invalid")
                .description("Total invalid messages received from the queue")
                .register(registry);
    }

    public void incrementValidMessageCounter() {
        validMessageCounter.increment();
    }

    public void incrementInvalidMessageCounter() {
        invalidMessageCounter.increment();
    }
}

