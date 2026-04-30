package com.universidad.estudiantes.repository;

import com.universidad.estudiantes.model.Estudiante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * Repositorio JPA que hereda todas las operaciones CRUD de JpaRepository.
 * Spring Data genera la implementación automáticamente en tiempo de ejecución.
 */
@Repository
public interface EstudianteRepository extends JpaRepository<Estudiante, Long> {

    // Consulta derivada: buscar por carrera (ignora mayúsculas/minúsculas)
    List<Estudiante> findByCarreraIgnoreCase(String carrera);

    // Consulta derivada: buscar por nombre o apellido (búsqueda parcial)
    List<Estudiante> findByNombreContainingOrApellidoContaining(
            String nombre, String apellido);
}