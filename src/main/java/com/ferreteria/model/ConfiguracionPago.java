package com.ferreteria.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

// Singleton: se espera un unico registro en toda la tabla (ver ConfiguracionPagoService).
@Entity
@Table(name = "configuracion_pago")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class ConfiguracionPago {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "whatsapp_pagos")
    private String whatsappPagos;

    @Column(name = "qr_yape_url", length = 500)
    private String qrYapeUrl;

    @Column(name = "banco")
    private String banco;

    @Column(name = "numero_cuenta")
    private String numeroCuenta;

    @Column(name = "cci")
    private String cci;

    @UpdateTimestamp
    @Column(name = "fecha_actualizacion", nullable = false)
    private LocalDateTime fechaActualizacion;
}
