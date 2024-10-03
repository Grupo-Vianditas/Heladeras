package ar.edu.utn.dds.k3003.RabbitMQConsumer.Implementations;

import ar.edu.utn.dds.k3003.RabbitMQConsumer.Interfaces.MessageHandler;
import ar.edu.utn.dds.k3003.app.Fachada;
import ar.edu.utn.dds.k3003.facades.dtos.TemperaturaDTO;
import ar.edu.utn.dds.k3003.presentation.metrics.MetricsConfig;
import ar.edu.utn.dds.k3003.presentation.metrics.queueCounters.QueueCounter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;


import java.util.NoSuchElementException;


public class TemperaturaMessageHandler implements MessageHandler {

    private Fachada fachada;
    private QueueCounter queueCounter;

    public TemperaturaMessageHandler(Fachada fachada, QueueCounter queueCounter){
        this.fachada = fachada;
        this.queueCounter = queueCounter;
    }

    @Override
    public void handleMessage(String message) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());

            TemperaturaDTO tempMessage = objectMapper.readValue(message, TemperaturaDTO.class);

            fachada.temperatura(tempMessage);
            queueCounter.incrementValidMessageCounter();
        }catch (NoSuchElementException nsee){
            System.out.println("Error en TemperaturaMessageHandler : " + nsee);
            queueCounter.incrementInvalidMessageCounter();
        }catch (Exception e){
            System.out.println("Error en TemperaturaMessageHandler : " + e);
            queueCounter.incrementInvalidMessageCounter();
        }
    }

}
