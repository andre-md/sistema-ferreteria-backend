package com.ferreteria.dto;

import com.ferreteria.model.enums.RolUsuario;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UsuarioRequest(

        @NotBlank
        String nombre,

        @NotBlank
        @Email
        String email,

        // Sin @NotBlank a proposito: en actualizar() un valor vacio/null significa
        // "no cambiar el password actual". En crear() se valida como obligatorio
        // explicitamente en el service, ya que ahi si es requerido.
        @Size(min = 6)
        String password,

        @NotNull
        RolUsuario rol
) {
}
