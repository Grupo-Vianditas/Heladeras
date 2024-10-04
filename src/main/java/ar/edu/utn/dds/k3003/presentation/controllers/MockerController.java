package ar.edu.utn.dds.k3003.presentation.controllers;

import ar.edu.utn.dds.k3003.app.Fachada;
import ar.edu.utn.dds.k3003.facades.dtos.HeladeraDTO;
import ar.edu.utn.dds.k3003.facades.dtos.TemperaturaDTO;
import ar.edu.utn.dds.k3003.presentation.auxiliar.ErrorHandler;
import ar.edu.utn.dds.k3003.presentation.metrics.controllersCounters.OthersCounter;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;

import java.time.LocalDateTime;
import java.util.Random;

public class MockerController {

    private final Fachada fachada;
    private OthersCounter mockerCounter;
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final int SALT_LENGTH = 5;

    public MockerController(Fachada fachada, OthersCounter mockerCounter) {
        this.fachada = fachada;
        this.mockerCounter = mockerCounter;
    }

    private String generateSalt() {
        Random random = new Random();
        StringBuilder salt = new StringBuilder(SALT_LENGTH);
        for (int i = 0; i < SALT_LENGTH; i++) {
            int randomIndex = random.nextInt(CHARACTERS.length());
            salt.append(CHARACTERS.charAt(randomIndex));
        }
        return salt.toString();
    }

    // ################
    // Update 4/10/24: respuesta de controller:
    // 0: Devuelve la lista de heladeras y 3 temperaturas aleatorias.
    // 3: Errores varios, no hay mucho para probar en este punto.
    // ################

    public void mockTestObjects(Context ctx) {
        try {

            List<Map<String, Object>> heladeras = new ArrayList<>();
            Random random = new Random();

            for (int i = 0; i < 4; i++) {
                Map<String, Object> heladeraMap = new HashMap<>();

                String salt = generateSalt();
                HeladeraDTO heladera = this.fachada.agregar(new HeladeraDTO("prueba_" + salt));

                Map<String, Object> heladeraData = new HashMap<>();
                heladeraData.put("id", heladera.getId());
                heladeraData.put("nombre", heladera.getNombre());
                heladeraData.put("viandas", heladera.getCantidadDeViandas());

                heladeraMap.put("heladera", heladeraData);

                List<Integer> listaDeTemperaturas = new ArrayList<>();
                for (int t = 0; t < 3; t++) {
                    int temperaturaAleatoria = random.nextInt(101) - 50;  // Generar entre -50 y 50
                    this.fachada.temperatura(new TemperaturaDTO(temperaturaAleatoria, heladera.getId(), LocalDateTime.now()));
                    listaDeTemperaturas.add(temperaturaAleatoria);
                }

                heladeraMap.put("temperaturas", listaDeTemperaturas);
                heladeras.add(heladeraMap);
            }

            Map<String, Object> jsonResponse = new HashMap<>();
            jsonResponse.put("heladeras", heladeras);

            ctx.json(jsonResponse);
            mockerCounter.incrementSuccessfulMockCounter();
        } catch (Exception e) {
            ErrorHandler.manejarError(ctx, HttpStatus.INTERNAL_SERVER_ERROR, 3, "Error no contemplado: " + e);
            mockerCounter.incrementFailedMockCounter();
        }
    }
}