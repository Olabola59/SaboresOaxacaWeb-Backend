package com.bluit.tourgatronomico.controller;

import com.bluit.tourgatronomico.service.BusquedaService;
import com.bluit.tourgatronomico.model.Usuario;
import com.bluit.tourgatronomico.model.Rol;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/busqueda")
public class BusquedaController {

    @Autowired
    private BusquedaService busquedaService;

    // Buscar usuarios por nombre
    @GetMapping("/usuarios/nombre/{nombre}")
    public List<Usuario> buscarUsuariosPorNombre(@PathVariable String nombre) {
        return busquedaService.buscarUsuariosPorNombre(nombre);
    }

    // Buscar usuario por email
    @GetMapping("/usuarios/email/{email}")
    public Usuario buscarUsuarioPorEmail(@PathVariable String email) {
        return busquedaService.buscarUsuarioPorEmail(email);
    }

    // Buscar roles por nombre
    @GetMapping("/roles/nombre/{nombre}")
    public List<Rol> buscarRolesPorNombre(@PathVariable String nombre) {
        return busquedaService.buscarRolesPorNombre(nombre);
    }

    // Todos los usuarios
    @GetMapping("/usuarios")
    public List<Usuario> obtenerUsuarios() {
        return busquedaService.obtenerTodosUsuarios();
    }

    // Todos los roles
    @GetMapping("/roles")
    public List<Rol> obtenerRoles() {
        return busquedaService.obtenerTodosRoles();
    }
}
