package com.ferreteria.service;

import com.ferreteria.dto.ConfiguracionPagoRequest;
import com.ferreteria.dto.ConfiguracionPagoResponse;
import com.ferreteria.mapper.ConfiguracionPagoMapper;
import com.ferreteria.model.ConfiguracionPago;
import com.ferreteria.repository.ConfiguracionPagoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ConfiguracionPagoService {

    private final ConfiguracionPagoRepository configuracionPagoRepository;

    // De solo lectura solo en el caso comun (el registro ya existe); si es la
    // primera vez que se usa el sistema, tambien necesita escribir para crearlo.
    @Transactional
    public ConfiguracionPagoResponse obtener() {
        return ConfiguracionPagoMapper.toResponse(obtenerOCrear());
    }

    @Transactional
    public ConfiguracionPagoResponse actualizar(ConfiguracionPagoRequest request) {
        ConfiguracionPago configuracionPago = obtenerOCrear();
        ConfiguracionPagoMapper.actualizarEntity(configuracionPago, request);
        return ConfiguracionPagoMapper.toResponse(configuracionPagoRepository.save(configuracionPago));
    }

    private ConfiguracionPago obtenerOCrear() {
        return configuracionPagoRepository.findAll().stream()
                .findFirst()
                .orElseGet(() -> configuracionPagoRepository.save(new ConfiguracionPago()));
    }
}
