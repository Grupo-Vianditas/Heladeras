package ar.edu.utn.dds.k3003.presentation.auxiliar;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ErrorResponse {
    private int code;
    private String description;
}