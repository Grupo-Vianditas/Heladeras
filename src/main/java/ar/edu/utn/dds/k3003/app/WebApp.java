package ar.edu.utn.dds.k3003.app;

import ar.edu.utn.dds.k3003.RabbitMQConsumer.Implementations.DefaultErrorHandler;
import ar.edu.utn.dds.k3003.RabbitMQConsumer.Implementations.TemperaturaMessageHandler;
import ar.edu.utn.dds.k3003.RabbitMQConsumer.Interfaces.ErrorHandler;
import ar.edu.utn.dds.k3003.RabbitMQConsumer.Interfaces.MessageHandler;
import ar.edu.utn.dds.k3003.RabbitMQConsumer.RabbitMQCloudConsumer;
import ar.edu.utn.dds.k3003.clients.ViandasProxy;
import ar.edu.utn.dds.k3003.facades.dtos.Constants;
import ar.edu.utn.dds.k3003.presentation.controllers.*;
import ar.edu.utn.dds.k3003.presentation.metrics.MetricsConfig;
import ar.edu.utn.dds.k3003.presentation.metrics.controllersCounters.*;

import ar.edu.utn.dds.k3003.presentation.metrics.heartbeat.ApplicationHealthCheck;
import ar.edu.utn.dds.k3003.presentation.metrics.queueCounters.QueueCounter;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.javalin.Javalin;
import io.javalin.json.JavalinJackson;
import io.javalin.micrometer.MicrometerPlugin;

import io.micrometer.prometheusmetrics.PrometheusMeterRegistry;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeoutException;

public class WebApp {

    private static final String TOKEN = "token123";

    public static void main(String[] args) {

        var env = System.getenv();
        Fachada fachada = new Fachada();

        // Inicializar las metricas
        MetricsConfig metricsConfig = new MetricsConfig();
        PrometheusMeterRegistry registry = metricsConfig.getRegistry();

        var objectMapper = createObjectMapper();
        fachada.setViandasProxy(new ViandasProxy(objectMapper));

        var port = Integer.parseInt(env.getOrDefault("PORT", "8080"));

        ApplicationHealthCheck healthCheck = new ApplicationHealthCheck(registry);

        var app = Javalin.create(config -> {
            config.jsonMapper(new JavalinJackson().updateMapper(WebApp::configureObjectMapper));
            config.registerPlugin(new MicrometerPlugin(micrometerConfig -> micrometerConfig.registry = registry));
        }).start(port);

        // Instancio los controllers
        var heladerasController = new HeladerasController(fachada, new HeladerasCounter(metricsConfig));
        var viandasController = new ViandasController(fachada, new ViandasCounter(metricsConfig));
        var temperaturasController = new TemperaturasController(fachada, new TemperaturasCounter(metricsConfig));
        var mockerController = new MockerController(fachada, new OthersCounter(metricsConfig));
        var cleanerController = new CleanerController(fachada, new OthersCounter(metricsConfig));
        var incidentesController = new IncidentesController(fachada, new IncidentesCounter(metricsConfig));

        // HeladerasController
        app.post("/heladeras", heladerasController::agregar);
        app.get("/heladeras/{heladeraId}", heladerasController::buscarXId);

        // Entrega 5
        app.post("/heladeras/{heladeraId}/habilitar", heladerasController::habilitar);
        app.post("/heladeras/{heladeraId}/inhabilitar", heladerasController::inhabilitar);

        // ViandasController
        app.post("/depositos", viandasController::depositar);
        app.post("/retiros", viandasController::retirar);

        // TemperaturasController
        app.post("/temperaturas", temperaturasController::registrarTemperatura);
        app.get("/heladeras/{heladeraId}/temperaturas", temperaturasController::obtenerTemperaturas);

        // MockController
        app.post("/mock", mockerController::mockTestObjects);

        // CleanerController
        app.post("/clear", cleanerController::clear);

        // IncidentesController
        app.post("/incidentes/{heladeraId}/temperatura/", incidentesController::alertarTemperatura);
        app.post("/incidentes/{heladeraId}/fraude", incidentesController::alertarFraude);
        app.post("/incidentes/{heladeraId}/conexion",incidentesController::alertarConexion );
        app.post("/incidentes/{heladeraId}/falla",incidentesController::falloTecnico);

        // Controller metricas
        app.get("/metrics", ctx -> {
            var auth = ctx.header("Authorization");

            if (auth != null && auth.intern().equals("Bearer " + TOKEN)) {
                ctx.contentType("text/plain; version=0.0.4")
                        .result(registry.scrape());
            } else {
                ctx.status(401).json("unauthorized access");
            }
        }
        );

        app.get("/", ctx -> ctx.result("Hola, soy una API y no un easter egg."));

        // Seteo el cliente del Rabbit
        MessageHandler messageHandler = new TemperaturaMessageHandler(fachada, new QueueCounter(metricsConfig));
        ErrorHandler errorHandler = new DefaultErrorHandler();

        RabbitMQCloudConsumer consumer = new RabbitMQCloudConsumer(
                env.get("QUEUE_NAME"),
                env.get("QUEUE_HOST"),
                env.get("QUEUE_USERNAME"),
                env.get("QUEUE_PASSWORD"),
                messageHandler,
                errorHandler
        );

        try {
            consumer.iniciarConexion();
            consumer.consumirMensajes();

            Thread.sleep(Long.MAX_VALUE);
        } catch (Exception e) {
            System.err.println("Error inicializando consumidor: " + e.getMessage());
        } finally {
            try {
                consumer.cerrarConexion();
            } catch (IOException | TimeoutException e) {
                System.err.println("Error cerrando conexión: " + e.getMessage());
            }
        }

    }

    public static ObjectMapper createObjectMapper() {
        var objectMapper = new ObjectMapper();
        configureObjectMapper(objectMapper);
        return objectMapper;
    }

    public static void configureObjectMapper(ObjectMapper objectMapper) {
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        var sdf = new SimpleDateFormat(Constants.DEFAULT_SERIALIZATION_FORMAT, Locale.getDefault());
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        objectMapper.setDateFormat(sdf);
    }

}