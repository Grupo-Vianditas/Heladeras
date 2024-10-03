package ar.edu.utn.dds.k3003.presentation.metrics.controllersCounters;

import ar.edu.utn.dds.k3003.presentation.metrics.MetricsConfig;
import io.micrometer.core.instrument.Counter;
import io.micrometer.prometheusmetrics.PrometheusMeterRegistry;

public class HeladerasCounter {

    // Contadores generales para GET y POST
    // Los tengo separados por exitosos y por fallidos

    // Exitosos
    private final Counter postSucessfulHeladerasCounter;
    private final Counter getSucessfulHeladerasCounter;

    // Fallidos
    private final Counter postFailedHeladerasCounter;
    private final Counter getFailedHeladerasCounter;

    public HeladerasCounter(MetricsConfig metricsConfig) {
        PrometheusMeterRegistry registry = metricsConfig.getRegistry();

        // Contadores para el endpoint POST /heladeras
        postSucessfulHeladerasCounter = Counter.builder("requests_post_heladeras")
                .tag("endpoint", "/heladeras")
                .tag("status","successful")
                .tag("method", "POST")
                .description("Total successful POST requests to /heladeras")
                .register(registry);

        postFailedHeladerasCounter = Counter.builder("requests_post_heladeras")
                .tag("endpoint", "/heladeras")
                .tag("status","failed")
                .tag("method", "POST")
                .description("Total failed POST requests to /heladeras")
                .register(registry);


        // Contadores para el endpoint GET /heladeras/{heladeraId}
        getSucessfulHeladerasCounter = Counter.builder("requests_get_heladeras")
                .tag("endpoint", "/heladeras/{heladeraId}")
                .tag("status","successful")
                .tag("method", "GET")
                .description("Total successful GET requests to /heladeras/{heladeraId}")
                .register(registry);

        getFailedHeladerasCounter = Counter.builder("requests_get_heladeras")
                .tag("endpoint", "/heladeras/{heladeraId}")
                .tag("status","failed")
                .tag("method", "GET")
                .description("Total failed GET requests to /heladeras/{heladeraId}")
                .register(registry);
    }

    public void incrementSucessfulPostCounter() {
        postSucessfulHeladerasCounter.increment();
    }

    public void incrementFailedPostCounter() {
        postFailedHeladerasCounter.increment();
    }

    public void incrementSuccessfulGetCounter() {
        getSucessfulHeladerasCounter.increment();
    }

    public void incrementFailedGetCounter() {
        getFailedHeladerasCounter.increment();
    }

}

