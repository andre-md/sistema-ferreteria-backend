package com.ferreteria.service;

import com.ferreteria.dto.CategoriaRequest;
import com.ferreteria.dto.CategoriaResponse;
import com.ferreteria.exception.CategoriaConProductosException;
import com.ferreteria.exception.CategoriaDuplicadaException;
import com.ferreteria.exception.CategoriaNoEncontradaException;
import com.ferreteria.mapper.CategoriaMapper;
import com.ferreteria.model.Categoria;
import com.ferreteria.repository.CategoriaRepository;
import com.ferreteria.repository.ProductoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;
    private final ProductoRepository productoRepository;

    public List<CategoriaResponse> listarTodas() {
        return CategoriaMapper.toResponseList(categoriaRepository.findAll());
    }

    public CategoriaResponse obtenerPorId(Long id) {
        return CategoriaMapper.toResponse(buscarCategoriaOLanzar(id));
    }

    @Transactional
    public CategoriaResponse crear(CategoriaRequest request) {
        if (categoriaRepository.existsByNombre(request.nombre())) {
            throw new CategoriaDuplicadaException("Ya existe una categoria con el nombre: " + request.nombre());
        }
        Categoria categoria = CategoriaMapper.toEntity(request);
        return CategoriaMapper.toResponse(categoriaRepository.save(categoria));
    }

    @Transactional
    public CategoriaResponse actualizar(Long id, CategoriaRequest request) {
        Categoria categoria = buscarCategoriaOLanzar(id);
        CategoriaMapper.actualizarEntity(categoria, request);
        return CategoriaMapper.toResponse(categoriaRepository.save(categoria));
    }

    @Transactional
    public void eliminar(Long id) {
        Categoria categoria = buscarCategoriaOLanzar(id);
        if (productoRepository.existsByCategoriaId(id)) {
            throw new CategoriaConProductosException(
                    "No se puede eliminar la categoria '" + categoria.getNombre() + "' porque tiene productos asociados");
        }
        categoriaRepository.delete(categoria);
    }

    private Categoria buscarCategoriaOLanzar(Long id) {
        return categoriaRepository.findById(id)
                .orElseThrow(() -> new CategoriaNoEncontradaException("Categoria no encontrada con id: " + id));
    }
}
