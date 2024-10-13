package ar.edu.utn.dds.k3003.model.errors;

public class OperacionInvalidaException extends RuntimeException {
    private final ErrorTipo errorTipo;

    public OperacionInvalidaException(ErrorTipo errorTipo, String mensaje) {
        super(mensaje);
        this.errorTipo = errorTipo;
    }

    public ErrorTipo getErrorTipo() {
        return errorTipo;
    }
}
