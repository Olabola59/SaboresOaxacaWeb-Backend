package com.bluit.tourgatronomico.service;

import com.bluit.tourgatronomico.model.Usuario;
import com.bluit.tourgatronomico.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    // Buscar usuario por email
    public Optional<Usuario> findByEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }

    // Registrar un nuevo usuario con validación
    public Usuario registrarUsuario(Usuario usuario) {
        if (usuarioRepository.findByEmail(usuario.getEmail()).isPresent()) {
            throw new RuntimeException("El email ya está registrado");
        }

        usuario.setFechaRegistro(LocalDateTime.now());
        return usuarioRepository.save(usuario);
    }

    // Guardar hora de último acceso/login
    public void registrarAcceso(Usuario usuario) {
        usuario.setUltimoAcceso(LocalDateTime.now());
        usuarioRepository.save(usuario);
    }

    // Guardar hora de invitación
    public void registrarInvitacion(Usuario usuario) {
        usuario.setFechaUltimaInvitacion(LocalDateTime.now());
        usuarioRepository.save(usuario);
    }
}
