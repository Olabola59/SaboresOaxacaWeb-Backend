package com.bluit.tourgatronomico.controller;

import com.bluit.tourgatronomico.model.Rol;
import com.bluit.tourgatronomico.repository.RolRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/roles")
public class RolController {

    private final RolRepository rolRepository;

    @Autowired
    public RolController(RolRepository rolRepository) {
        this.rolRepository = rolRepository;
    }

    // Obtener todos los roles
    @GetMapping
    public ResponseEntity<List<Rol>> getAllRoles() {
        List<Rol> roles = rolRepository.findAll();
        return ResponseEntity.ok(roles);
    }

    // Crear un rol
    @PostMapping
    public ResponseEntity<?> createRol(@RequestBody Rol rol) {
        try {
            // Validar que el nombre no esté vacío
            if (rol.getNombre() == null || rol.getNombre().trim().isEmpty()) {
                Map<String, String> error = new HashMap<>();
                error.put("message", "El nombre del rol es obligatorio");
                return ResponseEntity.badRequest().body(error);
            }
            
            // Verificar si el rol ya existe
            if (rolRepository.findByNombre(rol.getNombre()).isPresent()) {
                Map<String, String> error = new HashMap<>();
                error.put("message", "Ya existe un rol con el nombre: " + rol.getNombre());
                return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
            }
            
            Rol rolGuardado = rolRepository.save(rol);
            return ResponseEntity.status(HttpStatus.CREATED).body(rolGuardado);
            
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", "Error al crear el rol: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    // Obtener un rol por ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getRolById(@PathVariable Long id) {
        try {
            Rol rol = rolRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Rol no encontrado con id " + id));
            return ResponseEntity.ok(rol);
            
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
    }

    // Obtener un rol por nombre
    @GetMapping("/nombre/{nombre}")
    public ResponseEntity<?> getRolByNombre(@PathVariable String nombre) {
        try {
            Rol rol = rolRepository.findByNombre(nombre)
                    .orElseThrow(() -> new RuntimeException("Rol no encontrado con nombre " + nombre));
            return ResponseEntity.ok(rol);
            
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
    }

    // Actualizar un rol
    @PutMapping("/{id}")
    public ResponseEntity<?> updateRol(@PathVariable Long id, @RequestBody Rol rolDetails) {
        try {
            Rol rol = rolRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Rol no encontrado con id " + id));

            // Validar que el nuevo nombre no esté vacío
            if (rolDetails.getNombre() == null || rolDetails.getNombre().trim().isEmpty()) {
                Map<String, String> error = new HashMap<>();
                error.put("message", "El nombre del rol es obligatorio");
                return ResponseEntity.badRequest().body(error);
            }

            // Verificar si el nuevo nombre ya existe en otro rol
            if (!rol.getNombre().equals(rolDetails.getNombre())) {
                if (rolRepository.findByNombre(rolDetails.getNombre()).isPresent()) {
                    Map<String, String> error = new HashMap<>();
                    error.put("message", "Ya existe otro rol con el nombre: " + rolDetails.getNombre());
                    return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
                }
            }

            rol.setNombre(rolDetails.getNombre());
            Rol rolActualizado = rolRepository.save(rol);
            return ResponseEntity.ok(rolActualizado);
            
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
    }

    // Eliminar un rol
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteRol(@PathVariable Long id) {
        try {
            Rol rol = rolRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Rol no encontrado con id " + id));
            rolRepository.delete(rol);
            
            Map<String, String> response = new HashMap<>();
            response.put("message", "Rol con id " + id + " eliminado correctamente");
            return ResponseEntity.ok(response);
            
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
    }
}