package com.ferreteria.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "saldos_cliente")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class SaldoCliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Identificador del cliente: no tenemos entidad Cliente propia, y el telefono
    // es lo unico razonablemente consistente que se captura en cada pedido.
    @NotBlank
    @Column(name = "cliente_telefono", nullable = false, unique = true)
    private String clienteTelefono;

    @NotBlank
    @Column(name = "cliente_nombre", nullable = false)
    private String clienteNombre;

    @NotNull
    @DecimalMin(value = "0", inclusive = true)
    @Builder.Default
    @Column(name = "monto_disponible", nullable = false, precision = 12, scale = 2)
    private BigDecimal montoDisponible = BigDecimal.ZERO;

    @CreationTimestamp
    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    @UpdateTimestamp
    @Column(name = "fecha_actualizacion", nullable = false)
    private LocalDateTime fechaActualizacion;
}
