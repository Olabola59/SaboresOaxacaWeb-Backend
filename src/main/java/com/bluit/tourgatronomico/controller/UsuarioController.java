package com.bluit.tourgatronomico.controller;

import com.bluit.tourgatronomico.model.Usuario;
import com.bluit.tourgatronomico.repository.UsuarioRepository;
import com.bluit.tourgatronomico.repository.ResenaRepository;
import com.bluit.tourgatronomico.repository.ResenaLikeRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.*;
import java.util.*;

@RestController
@RequestMapping("/api/usuarios")
@CrossOrigin(origins = "*") // si ya tienes config global CORS, puedes quitar esto
public class UsuarioController {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final ResenaRepository resenaRepository;
    private final ResenaLikeRepository resenaLikeRepository;

    @Autowired
    public UsuarioController(
            UsuarioRepository usuarioRepository,
            PasswordEncoder passwordEncoder,
            ResenaRepository resenaRepository,
            ResenaLikeRepository resenaLikeRepository
    ) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.resenaRepository = resenaRepository;
        this.resenaLikeRepository = resenaLikeRepository;
    }

    // ===================== REGISTRO =====================
    @PostMapping("/registro")
    public ResponseEntity<?> registrar(@RequestBody Usuario usuario) {
        try {
            if (usuario.getEmail() == null || usuario.getEmail().trim().isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("message", "El email es obligatorio"));
            }
            if (usuario.getPassword() == null || usuario.getPassword().trim().isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("message", "La contraseña es obligatoria"));
            }

            // Evitar duplicado por email
            Optional<Usuario> existente = usuarioRepository.findByEmail(usuario.getEmail().trim());
            if (existente.isPresent()) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(Map.of("message", "El email ya está registrado"));
            }

            usuario.setEmail(usuario.getEmail().trim().toLowerCase());
            usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));

            Usuario saved = usuarioRepository.save(usuario);
            saved.setPassword(null); // nunca devuelvas password

            return ResponseEntity.status(HttpStatus.CREATED).body(saved);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Error al registrar usuario: " + e.getMessage()));
        }
    }

    // ===================== LOGIN =====================
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> body) {
        try {
            String email = body.getOrDefault("email", "").trim().toLowerCase();
            String password = body.getOrDefault("password", "").trim();

            if (email.isEmpty() || password.isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("message", "Email y contraseña son obligatorios"));
            }

            Usuario usuario = usuarioRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("Credenciales inválidas"));

            if (!passwordEncoder.matches(password, usuario.getPassword())) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("message", "Credenciales inválidas"));
            }

            usuario.setPassword(null);

            // Si tú ya usas token/JWT, aquí sería donde lo generas y lo devuelves.
            // Por ahora devolvemos usuario.
            return ResponseEntity.ok(usuario);

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Error al iniciar sesión: " + e.getMessage()));
        }
    }

    // ===================== OBTENER USUARIO =====================
    @GetMapping("/{id}")
    public ResponseEntity<?> getUsuario(@PathVariable Long id) {
        try {
            Usuario usuario = usuarioRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado con id " + id));

            usuario.setPassword(null);
            return ResponseEntity.ok(usuario);

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Error obteniendo usuario: " + e.getMessage()));
        }
    }

    // ===================== ACTUALIZAR INFO BÁSICA =====================
    @PutMapping("/{id}")
    public ResponseEntity<?> updateUsuario(@PathVariable Long id, @RequestBody Usuario changes) {
        try {
            Usuario usuario = usuarioRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado con id " + id));

            // Actualiza solo campos permitidos (ajusta según tu modelo)
            if (changes.getNombre() != null) usuario.setNombre(changes.getNombre());
            if (changes.getEmail() != null && !changes.getEmail().trim().isEmpty()) {
                usuario.setEmail(changes.getEmail().trim().toLowerCase());
            }

            // Si quieres permitir cambiar contraseña, hazlo con endpoint separado.
            Usuario saved = usuarioRepository.save(usuario);
            saved.setPassword(null);

            return ResponseEntity.ok(saved);

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Error actualizando usuario: " + e.getMessage()));
        }
    }

    // ===================== PERFIL (STATS) =====================
    // Devuelve: usuario + totalComentarios (reseñas) + totalLikesRecibidos (likes a sus reseñas)
    @GetMapping("/{id}/perfil")
    public ResponseEntity<?> getPerfil(@PathVariable Long id) {
        try {
            Usuario usuario = usuarioRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado con id " + id));

            usuario.setPassword(null);

            long totalComentarios = resenaRepository.countByUsuarioId(id);
            long totalLikesRecibidos = resenaLikeRepository.countLikesRecibidosByUsuario(id);

            Map<String, Object> resp = new HashMap<>();
            resp.put("usuario", usuario);
            resp.put("totalComentarios", totalComentarios);
            resp.put("totalLikesRecibidos", totalLikesRecibidos);

            return ResponseEntity.ok(resp);

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Error cargando perfil: " + e.getMessage()));
        }
    }

    // ===================== SUBIR FOTO DE PERFIL =====================
    // Guarda en carpeta "uploads/" y almacena ruta pública en usuario.foto = "/uploads/archivo.ext"
    @PostMapping("/{id}/foto")
    public ResponseEntity<?> uploadFoto(@PathVariable Long id, @RequestParam("file") MultipartFile file) {
        try {
            if (file == null || file.isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("message", "Archivo vacío"));
            }

            Usuario usuario = usuarioRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado con id " + id));

            String original = file.getOriginalFilename() == null ? "foto" : file.getOriginalFilename();
            String ext = "";
            int dot = original.lastIndexOf('.');
            if (dot >= 0) ext = original.substring(dot);

            String filename = "user_" + id + "_" + UUID.randomUUID() + ext;

            Path uploadDir = Paths.get("uploads");
            if (!Files.exists(uploadDir)) Files.createDirectories(uploadDir);

            Path target = uploadDir.resolve(filename);
            Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);

            usuario.setFoto("/uploads/" + filename);

            Usuario saved = usuarioRepository.save(usuario);
            saved.setPassword(null);

            return ResponseEntity.ok(saved);

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Error subiendo foto: " + e.getMessage()));
        }
    }
}
