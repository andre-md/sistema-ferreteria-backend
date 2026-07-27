package com.ferreteria.service;

import com.ferreteria.dto.ProveedorRequest;
import com.ferreteria.dto.ProveedorResponse;
import com.ferreteria.exception.ProveedorConCotizacionesException;
import com.ferreteria.exception.ProveedorDuplicadoException;
import com.ferreteria.exception.ProveedorNoEncontradoException;
import com.ferreteria.mapper.ProveedorMapper;
import com.ferreteria.model.Proveedor;
import com.ferreteria.repository.ProductoProveedorRepository;
import com.ferreteria.repository.ProveedorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProveedorService {

    private final ProveedorRepository proveedorRepository;
    private final ProductoProveedorRepository productoProveedorRepository;

    public List<ProveedorResponse> listarTodos() {
        return ProveedorMapper.toResponseList(proveedorRepository.findAll());
    }

    public ProveedorResponse obtenerPorId(Long id) {
        return ProveedorMapper.toResponse(buscarProveedorOLanzar(id));
    }

    @Transactional
    public ProveedorResponse crear(ProveedorRequest request) {
        // Solo se valida duplicado cuando el nuevo proveedor SI trae contactoWhatsapp:
        // si no lo trae, no hay forma de distinguirlo de otro contacto del mismo
        // distribuidor, asi que se permite crear (ver reglas de negocio del modulo).
        String contactoWhatsapp = request.contactoWhatsapp();
        if (contactoWhatsapp != null && !contactoWhatsapp.isBlank()
                && proveedorRepository.existsByNombreAndContactoWhatsapp(request.nombre(), contactoWhatsapp)) {
            throw new ProveedorDuplicadoException(
                    "Ya existe un proveedor '" + request.nombre() + "' con el contacto de WhatsApp " + contactoWhatsapp);
        }

        Proveedor proveedor = ProveedorMapper.toEntity(request);
        return ProveedorMapper.toResponse(proveedorRepository.save(proveedor));
    }

    @Transactional
    public ProveedorResponse actualizar(Long id, ProveedorRequest request) {
        Proveedor proveedor = buscarProveedorOLanzar(id);
        ProveedorMapper.actualizarEntity(proveedor, request);
        return ProveedorMapper.toResponse(proveedorRepository.save(proveedor));
    }

    @Transactional
    public void eliminar(Long id) {
        Proveedor proveedor = buscarProveedorOLanzar(id);
        if (productoProveedorRepository.existsByProveedorId(id)) {
            throw new ProveedorConCotizacionesException(
                    "No se puede eliminar el proveedor '" + proveedor.getNombre() + "' porque tiene cotizaciones de productos asociadas");
        }
        proveedorRepository.delete(proveedor);
    }

    private Proveedor buscarProveedorOLanzar(Long id) {
        return proveedorRepository.findById(id)
                .orElseThrow(() -> new ProveedorNoEncontradoException("Proveedor no encontrado con id: " + id));
    }
}
