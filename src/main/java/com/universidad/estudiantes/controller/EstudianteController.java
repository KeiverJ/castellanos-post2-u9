package com.universidad.estudiantes.controller;

import com.universidad.estudiantes.model.Estudiante;
import com.universidad.estudiantes.service.EstudianteService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador MVC que maneja todas las rutas bajo /estudiantes.
 * Usa @Valid y BindingResult para validar el formulario en el servidor.
 */
@Controller
@RequestMapping("/estudiantes")
public class EstudianteController {

    private final EstudianteService service;

    public EstudianteController(EstudianteService service) {
        this.service = service;
    }

    // GET /estudiantes → lista todos los estudiantes
    @GetMapping
    public String listar(Model model) {
        model.addAttribute("estudiantes", service.listarTodos());
        return "estudiantes/lista";
    }

    // GET /estudiantes/nuevo → muestra formulario vacío
    @GetMapping("/nuevo")
    public String mostrarFormularioNuevo(Model model) {
        model.addAttribute("estudiante", new Estudiante());
        model.addAttribute("titulo", "Nuevo Estudiante");
        return "estudiantes/formulario";
    }

    // POST /estudiantes/guardar → valida y guarda, o muestra errores
    @PostMapping("/guardar")
    public String guardar(@Valid @ModelAttribute Estudiante estudiante,
            BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("titulo", estudiante.getId() == null
                    ? "Nuevo Estudiante"
                    : "Editar Estudiante");
            return "estudiantes/formulario";
        }
        try {
            service.guardar(estudiante);
        } catch (Exception e) {
            model.addAttribute("titulo", estudiante.getId() == null
                    ? "Nuevo Estudiante"
                    : "Editar Estudiante");
            model.addAttribute("errorCorreo", "El correo ya está registrado.");
            return "estudiantes/formulario";
        }
        return "redirect:/estudiantes";
    }

    // GET /estudiantes/editar/{id} → muestra formulario prellenado
    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable Long id, Model model) {
        model.addAttribute("estudiante", service.buscarPorId(id));
        model.addAttribute("titulo", "Editar Estudiante");
        return "estudiantes/formulario";
    }

    // GET /estudiantes/eliminar/{id} → muestra confirmación de eliminación
    @GetMapping("/eliminar/{id}")
    public String confirmarEliminar(@PathVariable Long id, Model model) {
        model.addAttribute("estudiante", service.buscarPorId(id));
        return "estudiantes/confirmar-eliminar";
    }

    // POST /estudiantes/eliminar/{id} → elimina y redirige
    @PostMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return "redirect:/estudiantes";
    }
}