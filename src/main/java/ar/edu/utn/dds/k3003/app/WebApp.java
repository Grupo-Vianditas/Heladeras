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

import ar.edu.utn.dds.k3003.presentation.metrics.controllersCounters.MetricsFactory;
import ar.edu.utn.dds.k3003.presentation.metrics.queueCounters.QueueCounter;
import ar.edu.utn.dds.k3003.service.*;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.javalin.Javalin;
import io.javalin.http.HttpStatus;
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

        HeladeraService heladeraService = new HeladeraService();
        TemperaturaService temperaturaService = new TemperaturaService();
        IncidentesService incidentesService = new IncidentesService();
        ImpresionService impresionService = new ImpresionService();
        NotificadorService notificadorService = new NotificadorService();
        RetirosService retirosService = new RetirosService();

        Fachada fachada = new Fachada(heladeraService, temperaturaService, incidentesService, impresionService, notificadorService, retirosService);

        // Iniciazar las metricas
        MetricsConfig metricsConfig = new MetricsConfig();
        PrometheusMeterRegistry registry = metricsConfig.getRegistry();
        MetricsFactory metricsFactory = new MetricsFactory(registry);
        MetricsService metricsService = new MetricsService(metricsFactory);

        var objectMapper = createObjectMapper();
        fachada.setViandasProxy(new ViandasProxy(objectMapper));

        var port = Integer.parseInt(env.getOrDefault("PORT", "8080"));

        var app = Javalin.create(config -> {
            config.jsonMapper(new JavalinJackson().updateMapper(WebApp::configureObjectMapper));
            config.registerPlugin(new MicrometerPlugin(micrometerConfig -> micrometerConfig.registry = registry));
        }).start(port);


        // Instancio los controllers
        var heladerasController = new HeladerasController(fachada, metricsService);
        var viandasController = new ViandasController(fachada, metricsService);
        var temperaturasController = new TemperaturasController(fachada, metricsService);
        var mockerController = new MockerController(fachada, metricsService);
        var cleanerController = new CleanerController(fachada, metricsService);
        var incidentesController = new IncidentesController(fachada, metricsService);

        // HeladerasController
        app.post("/heladeras", heladerasController::agregar);
        app.get("/heladeras/{heladeraId}", heladerasController::buscarXId);

        // Entrega 5
        app.post("/heladeras/habilitar", heladerasController::habilitar);
        app.post("/heladeras/deshabilitar", heladerasController::deshabilitar);

        // ViandasController
        app.post("/depositos", viandasController::depositar);
        app.post("/retiros", viandasController::retirar);
        app.get("/retiros/{heladeraId}", viandasController::retiros);

        // TemperaturasController
        app.post("/temperaturas", temperaturasController::registrarTemperatura);
        app.get("/heladeras/{heladeraId}/temperaturas", temperaturasController::obtenerTemperaturas);

        // MockController
        app.post("/mock", mockerController::mockTestObjects);

        // CleanerController
        app.post("/clear", cleanerController::clear);

        // IncidentesController
        app.post("/incidentes/notificarAlerta", incidentesController::notificarAlerta);
        app.post("/incidentes/fallaTecnica",incidentesController::falloTecnico);
        app.get("/incidentes/{heladeraId}", incidentesController::getIncidentesByHeladeraId);


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
        app.get("/status", ctx -> ctx.status(HttpStatus.OK));


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

        CronService cron = new CronService(heladeraService);
        cron.startJob(env.getOrDefault("ENDPOINT_ALERTAS", "http://localhost:8080/incidentes/notificarAlerta"));

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