package ar.edu.utn.dds.k3003.RabbitMQConsumer.Implementations;

import ar.edu.utn.dds.k3003.RabbitMQConsumer.Interfaces.ErrorHandler;

public class DefaultErrorHandler implements ErrorHandler {

    @Override
    public void handleError(Exception e) {
        System.err.println("Ocurrió un error durante el procesamiento: " + e.getMessage());
    }
}
