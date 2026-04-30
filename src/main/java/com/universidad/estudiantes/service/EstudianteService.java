package com.universidad.estudiantes.service;

import com.universidad.estudiantes.model.Estudiante;
import com.universidad.estudiantes.repository.EstudianteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

/**
 * Servicio que contiene la lógica de negocio para la gestión de estudiantes.
 * 
 * @Transactional garantiza la integridad de los datos en operaciones de
 *                escritura.
 */
@Service
public class EstudianteService {

    private final EstudianteRepository repo;

    // Inyección de dependencias por constructor (recomendado por Spring)
    public EstudianteService(EstudianteRepository repo) {
        this.repo = repo;
    }

    public List<Estudiante> listarTodos() {
        return repo.findAll();
    }

    public Estudiante buscarPorId(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Estudiante no encontrado: " + id));
    }

    @Transactional
    public Estudiante guardar(Estudiante estudiante) {
        return repo.save(estudiante);
    }

    @Transactional
    public void eliminar(Long id) {
        repo.deleteById(id);
    }
}