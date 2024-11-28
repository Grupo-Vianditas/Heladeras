package ar.edu.utn.dds.k3003.presentation.controllers;

import ar.edu.utn.dds.k3003.app.Fachada;
import ar.edu.utn.dds.k3003.facades.dtos.TemperaturaDTO;
import ar.edu.utn.dds.k3003.presentation.auxiliar.DTOs.heladera.CreateHeladeraDTO;
import ar.edu.utn.dds.k3003.presentation.auxiliar.DTOs.heladera.ReturningHeladeraDTO;
import ar.edu.utn.dds.k3003.presentation.controllers.baseController.BaseController;
import ar.edu.utn.dds.k3003.service.MetricsService;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;

import java.util.Map;
import java.util.List;

import java.time.LocalDateTime;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class MockerController extends BaseController {

    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final int SALT_LENGTH = 5;
    private static final int MIN_TEMPERATURE = -50;
    private static final int MAX_TEMPERATURE = 50;
    private static final int NUM_HELADERAS = 4;
    private static final int NUM_TEMPERATURAS = 3;

    private final Fachada fachada;
    private final Random random;

    public MockerController(Fachada fachada, MetricsService metricsService) {
        super(metricsService);
        this.fachada = fachada;
        this.random = new Random();
    }

    public void mockTestObjects(Context ctx) {
        handleRequest(ctx, "/mocker/mockTestObjects", "GET", () -> {
            List<Map<String, Object>> heladeras = generateMockHeladeras(NUM_HELADERAS);
            ctx.json(Map.of("heladeras", heladeras));
        });
    }

    private List<Map<String, Object>> generateMockHeladeras(int count) {
        return IntStream.range(0, count)
                .mapToObj(i -> createMockHeladera())
                .toList();
    }

    private Map<String, Object> createMockHeladera() {
        String salt = generateSalt();
        ReturningHeladeraDTO heladera = fachada.agregar(new CreateHeladeraDTO("prueba_" + salt));

        return Map.of(
                "heladera", extractHeladeraData(heladera),
                "temperaturas", generateMockTemperaturas(heladera.getHeladeraId(), NUM_TEMPERATURAS)
        );
    }

    private Map<String, Object> extractHeladeraData(ReturningHeladeraDTO heladera) {
        return Map.of(
                "heladeraId", heladera.getHeladeraId(),
                "nombre", heladera.getNombre(),
                "viandas", heladera.getCantidadDeViandas(),
                "habilitacion", heladera.getHabilitacion()
        );
    }

    private List<Integer> generateMockTemperaturas(Integer heladeraId, int count) {
        return IntStream.range(0, count)
                .mapToObj(i -> createMockTemperatura(heladeraId))
                .toList();
    }

    private int createMockTemperatura(Integer heladeraId) {
        int temperatura = random.nextInt(MAX_TEMPERATURE - MIN_TEMPERATURE + 1) + MIN_TEMPERATURE;
        fachada.temperatura(new TemperaturaDTO(temperatura, heladeraId, LocalDateTime.now()));
        return temperatura;
    }

    private String generateSalt() {
        return random.ints(SALT_LENGTH, 0, CHARACTERS.length())
                .mapToObj(CHARACTERS::charAt)
                .map(String::valueOf)
                .collect(Collectors.joining());
    }
}
