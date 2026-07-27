package com.ferreteria.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CambiarPasswordRequest(

        @NotBlank
        String passwordActual,

        @NotBlank
        @Size(min = 6)
        String passwordNuevo
) {
}
