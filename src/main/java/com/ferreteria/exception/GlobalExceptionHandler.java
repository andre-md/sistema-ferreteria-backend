package com.ferreteria.exception;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ProductoNoEncontradoException.class)
    public ResponseEntity<ErrorResponse> handleProductoNoEncontrado(ProductoNoEncontradoException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorResponse.of(ex.getMessage()));
    }

    @ExceptionHandler(CategoriaNoEncontradaException.class)
    public ResponseEntity<ErrorResponse> handleCategoriaNoEncontrada(CategoriaNoEncontradaException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorResponse.of(ex.getMessage()));
    }

    @ExceptionHandler(ProveedorNoEncontradoException.class)
    public ResponseEntity<ErrorResponse> handleProveedorNoEncontrado(ProveedorNoEncontradoException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorResponse.of(ex.getMessage()));
    }

    @ExceptionHandler(CategoriaDuplicadaException.class)
    public ResponseEntity<ErrorResponse> handleCategoriaDuplicada(CategoriaDuplicadaException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ErrorResponse.of(ex.getMessage()));
    }

    @ExceptionHandler(CategoriaConProductosException.class)
    public ResponseEntity<ErrorResponse> handleCategoriaConProductos(CategoriaConProductosException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ErrorResponse.of(ex.getMessage()));
    }

    @ExceptionHandler(ProductoProveedorNoEncontradoException.class)
    public ResponseEntity<ErrorResponse> handleProductoProveedorNoEncontrado(ProductoProveedorNoEncontradoException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorResponse.of(ex.getMessage()));
    }

    @ExceptionHandler(ProductoProveedorDuplicadoException.class)
    public ResponseEntity<ErrorResponse> handleProductoProveedorDuplicado(ProductoProveedorDuplicadoException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ErrorResponse.of(ex.getMessage()));
    }

    @ExceptionHandler(TipoCambioNoDisponibleException.class)
    public ResponseEntity<ErrorResponse> handleTipoCambioNoDisponible(TipoCambioNoDisponibleException ex) {
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(ErrorResponse.of(ex.getMessage()));
    }

    @ExceptionHandler(UsuarioNoEncontradoException.class)
    public ResponseEntity<ErrorResponse> handleUsuarioNoEncontrado(UsuarioNoEncontradoException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorResponse.of(ex.getMessage()));
    }

    @ExceptionHandler(EmailDuplicadoException.class)
    public ResponseEntity<ErrorResponse> handleEmailDuplicado(EmailDuplicadoException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ErrorResponse.of(ex.getMessage()));
    }

    @ExceptionHandler(PasswordIncorrectoException.class)
    public ResponseEntity<ErrorResponse> handlePasswordIncorrecto(PasswordIncorrectoException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorResponse.of(ex.getMessage()));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgument(IllegalArgumentException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorResponse.of(ex.getMessage()));
    }

    @ExceptionHandler(PedidoNoEncontradoException.class)
    public ResponseEntity<ErrorResponse> handlePedidoNoEncontrado(PedidoNoEncontradoException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorResponse.of(ex.getMessage()));
    }

    @ExceptionHandler(StockInsuficienteException.class)
    public ResponseEntity<ErrorResponse> handleStockInsuficiente(StockInsuficienteException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ErrorResponse.of(ex.getMessage()));
    }

    @ExceptionHandler(PedidoNoPagadoException.class)
    public ResponseEntity<ErrorResponse> handlePedidoNoPagado(PedidoNoPagadoException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ErrorResponse.of(ex.getMessage()));
    }

    @ExceptionHandler(PedidoYaAsignadoException.class)
    public ResponseEntity<ErrorResponse> handlePedidoYaAsignado(PedidoYaAsignadoException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ErrorResponse.of(ex.getMessage()));
    }

    @ExceptionHandler(PedidoYaPagadoException.class)
    public ResponseEntity<ErrorResponse> handlePedidoYaPagado(PedidoYaPagadoException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ErrorResponse.of(ex.getMessage()));
    }

    @ExceptionHandler(PedidoYaCanceladoException.class)
    public ResponseEntity<ErrorResponse> handlePedidoYaCancelado(PedidoYaCanceladoException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ErrorResponse.of(ex.getMessage()));
    }

    @ExceptionHandler(ProveedorConCotizacionesException.class)
    public ResponseEntity<ErrorResponse> handleProveedorConCotizaciones(ProveedorConCotizacionesException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ErrorResponse.of(ex.getMessage()));
    }

    @ExceptionHandler(ProveedorDuplicadoException.class)
    public ResponseEntity<ErrorResponse> handleProveedorDuplicado(ProveedorDuplicadoException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ErrorResponse.of(ex.getMessage()));
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleBadCredentials(BadCredentialsException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ErrorResponse.of("Email o password incorrectos"));
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorResponse> handleMissingParam(MissingServletRequestParameterException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ErrorResponse.of("Falta el parametro requerido: " + ex.getParameterName()));
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ErrorResponse.of("El valor '" + ex.getValue() + "' no es valido para el parametro '" + ex.getName() + "'"));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleMessageNotReadable(HttpMessageNotReadableException ex) {
        Throwable causaRaiz = ex.getMostSpecificCause();
        String mensaje = "El cuerpo de la peticion no es valido o esta mal formado";

        if (causaRaiz instanceof InvalidFormatException invalidFormat && invalidFormat.getTargetType().isEnum()) {
            mensaje = "Valor '" + invalidFormat.getValue() + "' invalido para el campo '"
                    + ultimoCampo(invalidFormat) + "'. Valores permitidos: "
                    + Arrays.toString(invalidFormat.getTargetType().getEnumConstants());
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorResponse.of(mensaje));
    }

    private String ultimoCampo(InvalidFormatException ex) {
        if (ex.getPath().isEmpty()) {
            return "desconocido";
        }
        return ex.getPath().get(ex.getPath().size() - 1).getFieldName();
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorResponse> handleMethodNotSupported(HttpRequestMethodNotSupportedException ex) {
        String metodosPermitidos = ex.getSupportedMethods() != null
                ? String.join(", ", ex.getSupportedMethods())
                : "ninguno";
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED)
                .body(ErrorResponse.of("Metodo " + ex.getMethod() + " no soportado para esta ruta. Metodos permitidos: " + metodosPermitidos));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationErrorResponse> handleValidationErrors(MethodArgumentNotValidException ex) {
        Map<String, String> errores = new LinkedHashMap<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errores.put(error.getField(), error.getDefaultMessage());
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ValidationErrorResponse.of("Error de validacion", errores));
    }
}
