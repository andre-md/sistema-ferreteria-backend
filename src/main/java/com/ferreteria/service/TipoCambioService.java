package com.ferreteria.service;

import com.ferreteria.dto.TipoCambioRequest;
import com.ferreteria.dto.TipoCambioResponse;
import com.ferreteria.exception.TipoCambioNoDisponibleException;
import com.ferreteria.mapper.TipoCambioMapper;
import com.ferreteria.model.TipoCambio;
import com.ferreteria.model.Usuario;
import com.ferreteria.repository.TipoCambioRepository;
import com.ferreteria.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TipoCambioService {

    private final TipoCambioRepository tipoCambioRepository;
    private final UsuarioRepository usuarioRepository;

    public TipoCambioResponse obtenerActual() {
        TipoCambio tipoCambio = tipoCambioRepository.findTopByOrderByFechaDesc()
                .orElseThrow(() -> new TipoCambioNoDisponibleException("No hay ningun tipo de cambio registrado"));
        return TipoCambioMapper.toResponse(tipoCambio);
    }

    public List<TipoCambioResponse> listarHistorial() {
        return TipoCambioMapper.toResponseList(tipoCambioRepository.findAllByOrderByFechaDesc());
    }

    @Transactional
    public TipoCambioResponse registrar(TipoCambioRequest request, String emailUsuarioAutenticado) {
        Usuario usuario = usuarioRepository.findByEmail(emailUsuarioAutenticado)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + emailUsuarioAutenticado));

        TipoCambio tipoCambio = TipoCambioMapper.toEntity(request, usuario);
        return TipoCambioMapper.toResponse(tipoCambioRepository.save(tipoCambio));
    }
}
