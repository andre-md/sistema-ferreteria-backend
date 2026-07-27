package com.ferreteria.service;

import com.ferreteria.dto.CambiarPasswordRequest;
import com.ferreteria.dto.UsuarioRequest;
import com.ferreteria.dto.UsuarioResponse;
import com.ferreteria.exception.EmailDuplicadoException;
import com.ferreteria.exception.PasswordIncorrectoException;
import com.ferreteria.exception.UsuarioNoEncontradoException;
import com.ferreteria.mapper.UsuarioMapper;
import com.ferreteria.model.Usuario;
import com.ferreteria.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UsuarioService {

    //Llama a la clase UsuarioRepository y PasswordEncoder para poder realizar las operaciones, usando de nombre de variable usuarioRepository y passwordEncoder
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    //Lista los usuarios activos 
    public List<UsuarioResponse> listarActivos() {
        return UsuarioMapper.toResponseList(usuarioRepository.findByActivoTrue());
    }

    //Obtiene un usuario por su id, si no lo encuentra lanza una excepción
    public UsuarioResponse obtenerPorId(Long id) {
        return UsuarioMapper.toResponse(buscarUsuarioOLanzar(id));
    }

    //Crea un nuevo usuario, con correo y contraseña, si la contraseña es nula lanza una excepción o  si el correo es nulo lanza una excepción
    @Transactional
    public UsuarioResponse crear(UsuarioRequest request) {
        if (request.password() == null || request.password().isBlank()) {
            throw new IllegalArgumentException("El password es obligatorio para crear un usuario");
        }
        if (usuarioRepository.existsByEmail(request.email())) {
            throw new EmailDuplicadoException("Ya existe un usuario con el email: " + request.email());
        }

        String passwordHash = passwordEncoder.encode(request.password());
        Usuario usuario = UsuarioMapper.toEntity(request, passwordHash);
        return UsuarioMapper.toResponse(usuarioRepository.save(usuario));
    }

    //Actualizar un usuario existente, si la contraseña es vacia lanza excepción,
    @Transactional
    public UsuarioResponse actualizar(Long id, UsuarioRequest request) {
        Usuario usuario = buscarUsuarioOLanzar(id);

        //Si el email cambia y ya le pertenece a OTRO usuario, se rechaza antes de tocar la base
        if (!usuario.getEmail().equalsIgnoreCase(request.email()) && usuarioRepository.existsByEmail(request.email())) {
            throw new EmailDuplicadoException("Ya existe un usuario con el email: " + request.email());
        }

        UsuarioMapper.actualizarEntity(usuario, request);

        if (request.password() != null && !request.password().isBlank()) {
            usuario.setPasswordHash(passwordEncoder.encode(request.password()));
        }

        return UsuarioMapper.toResponse(usuarioRepository.save(usuario));
    }

    @Transactional
    public void desactivar(Long id) {
        Usuario usuario = buscarUsuarioOLanzar(id);
        usuario.setActivo(false);
        usuarioRepository.save(usuario);
    }

    @Transactional
    public void cambiarPassword(Long id, CambiarPasswordRequest request, String emailAutenticado, boolean esAdmin) {
        Usuario usuario = buscarUsuarioOLanzar(id);

        boolean esElMismoUsuario = usuario.getEmail().equalsIgnoreCase(emailAutenticado);
        if (!esElMismoUsuario && !esAdmin) {
            throw new AccessDeniedException("No puedes cambiar el password de otro usuario");
        }

        if (!passwordEncoder.matches(request.passwordActual(), usuario.getPasswordHash())) {
            throw new PasswordIncorrectoException("El password actual no es correcto");
        }

        usuario.setPasswordHash(passwordEncoder.encode(request.passwordNuevo()));
        usuarioRepository.save(usuario);
    }

    private Usuario buscarUsuarioOLanzar(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new UsuarioNoEncontradoException("Usuario no encontrado con id: " + id));
    }
}
