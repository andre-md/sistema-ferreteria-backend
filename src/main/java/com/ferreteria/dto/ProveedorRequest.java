package com.ferreteria.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record ProveedorRequest(

        @NotBlank
        String nombre,

        @Pattern(regexp = "^(\\+51)?9\\d{8}$", message = "El contacto de WhatsApp debe ser un celular peruano valido (9 digitos, con o sin prefijo +51)")
        String contactoWhatsapp
) {
}
