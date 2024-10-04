package ar.edu.utn.dds.k3003.presentation.auxiliar;

import io.javalin.http.Context;
import io.javalin.http.HttpStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
public class ErrorHandler {

    public static void manejarError(Context ctx, HttpStatus status, int code, String errorMessage) {
        ctx.status(status);
        ctx.json(createErrorResponse(code, "Failed", errorMessage));
    }

    private static Map<String, Object> createErrorResponse(int code, String status, String error) {
        return Map.of(
                "Code", code,
                "Status", status,
                "Error", error
        );
    }
}
