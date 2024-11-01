package ar.edu.utn.dds.k3003.infra;

import ar.edu.utn.dds.k3003.infra.eventTypes.EventType;

public class Event {
    private final EventType tipo;
    private final Object data;

    public Event(EventType tipo, Object data) {
        this.tipo = tipo;
        this.data = data;
    }

    public EventType getTipo() {
        return tipo;
    }

    public Object getData() {
        return data;
    }
}


