package ar.edu.utn.dds.k3003.presentation.metrics.heartbeat;

import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;

public class ApplicationHealthCheck {

    private boolean appAlive;

    public ApplicationHealthCheck(MeterRegistry registry) {
        // Inicialmente asumimos que la aplicación está viva
        this.appAlive = true;

        // Registramos un Gauge en Micrometer para monitorear el estado de la aplicación
        Gauge.builder("application_alive", this, healthCheck -> healthCheck.isAppAlive() ? 1.0 : 0.0)
                .description("Marca si la app esta viva (1 si, 0 no)")
                .register(registry);
    }

    public boolean isAppAlive() {
        return appAlive;
    }

    public void setAppAlive(boolean appAlive) {
        this.appAlive = appAlive;
    }

}