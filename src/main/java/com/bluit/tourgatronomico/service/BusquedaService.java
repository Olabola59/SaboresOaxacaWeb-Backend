package com.bluit.tourgatronomico.service;

import com.bluit.tourgatronomico.model.Usuario;
import com.bluit.tourgatronomico.model.Rol;
import com.bluit.tourgatronomico.repository.UsuarioRepository;
import com.bluit.tourgatronomico.repository.RolRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BusquedaService {

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;

    @Autowired
    public BusquedaService(UsuarioRepository usuarioRepository, RolRepository rolRepository) {
        this.usuarioRepository = usuarioRepository;
        this.rolRepository = rolRepository;
    }

    // Buscar usuarios por nombre (contiene)
    public List<Usuario> buscarUsuariosPorNombre(String nombre) {
        return usuarioRepository.findByNombreContainingIgnoreCase(nombre);
    }

    // Buscar usuario por email exacto
    public Usuario buscarUsuarioPorEmail(String email) {
        return usuarioRepository.findByEmail(email).orElse(null);
    }

    // Buscar roles por nombre
    public List<Rol> buscarRolesPorNombre(String nombre) {
        return rolRepository.findByNombreContainingIgnoreCase(nombre);
    }

    // Obtener todos los usuarios
    public List<Usuario> obtenerTodosUsuarios() {
        return usuarioRepository.findAll();
    }

    // Obtener todos los roles
    public List<Rol> obtenerTodosRoles() {
        return rolRepository.findAll();
    }
}
