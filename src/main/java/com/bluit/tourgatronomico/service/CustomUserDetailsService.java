package com.bluit.tourgatronomico.service;

import com.bluit.tourgatronomico.model.Usuario;
import com.bluit.tourgatronomico.repository.UsuarioRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    public CustomUserDetailsService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        System.out.println("🔍 Buscando usuario: " + email);
        
        Usuario usuario = usuarioRepository.findByEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + email));

        System.out.println("✅ Usuario encontrado: " + usuario.getEmail());
        System.out.println("🔐 Password hash en BD: " + usuario.getPassword());

        String role = (usuario.getRol() != null && usuario.getRol().getNombre() != null)
            ? usuario.getRol().getNombre()
            : "USER";
        
        if (!role.startsWith("ROLE_")) {
            role = "ROLE_" + role;
        }

        System.out.println("👤 Rol: " + role);

        return org.springframework.security.core.userdetails.User
            .withUsername(usuario.getEmail())
            .password(usuario.getPassword())
            .authorities(role)
            .build();
    }
}