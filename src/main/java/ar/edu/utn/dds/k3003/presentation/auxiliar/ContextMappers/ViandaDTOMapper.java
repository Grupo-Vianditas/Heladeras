package ar.edu.utn.dds.k3003.presentation.auxiliar.ContextMappers;


import org.json.JSONException;
import org.json.JSONObject;
import io.javalin.http.Context;
import ar.edu.utn.dds.k3003.facades.dtos.ViandaDTO;

public class ViandaDTOMapper {

    private static final String QR_VIANDA_KEY = "qrVianda";

    public ViandaDTO mapper(Context ctx) {
        String body = ctx.body();
        ViandaDTO viandaDTO = ctx.bodyAsClass(ViandaDTO.class);

        try {
            JSONObject jsonObject = new JSONObject(body);

            if (jsonObject.has(QR_VIANDA_KEY)) {
                viandaDTO.setCodigoQR(jsonObject.getString(QR_VIANDA_KEY));
            }
        } catch (JSONException e) {
            throw new IllegalArgumentException("JSON no valido", e);
        }

        return viandaDTO;
    }

}
