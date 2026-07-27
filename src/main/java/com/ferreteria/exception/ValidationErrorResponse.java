package com.ferreteria.exception;

import java.time.LocalDateTime;
import java.util.Map;

public record ValidationErrorResponse(String mensaje, LocalDateTime timestamp, Map<String, String> errores) {

    public static ValidationErrorResponse of(String mensaje, Map<String, String> errores) {
        return new ValidationErrorResponse(mensaje, LocalDateTime.now(), errores);
    }
}
