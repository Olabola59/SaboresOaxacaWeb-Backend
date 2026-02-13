package com.bluit.tourgatronomico.controller;



import com.bluit.tourgatronomico.model.Rol;
import com.bluit.tourgatronomico.model.Usuario;
import com.bluit.tourgatronomico.repository.RolRepository;
import com.bluit.tourgatronomico.repository.UsuarioRepository;
import com.bluit.tourgatronomico.service.EmailService;
import com.bluit.tourgatronomico.service.JwtService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired private AuthenticationManager authenticationManager;
    @Autowired private UsuarioRepository usuarioRepository;
    @Autowired private RolRepository rolRepository;
    @Autowired private PasswordEncoder passwordEncoder;
    @Autowired private JwtService jwtService;
    @Autowired private EmailService emailService;

    @Value("${app.frontend.base-url}")
    private String frontendBaseUrl;

    @Value("${app.email.verify.exp-hours:24}")
    private int verifyExpHours;

    // ✅ NUEVO: reset token expira (minutos)
    @Value("${app.email.reset.exp-minutes:30}")
    private int resetExpMinutes;

    // =========================
    // LOGIN
    // =========================
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> loginRequest) {
        try {
            String email = loginRequest.get("email");
            String password = loginRequest.get("password");

            if (email == null || email.trim().isEmpty() || password == null || password.trim().isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("message", "Email y contraseña son requeridos"));
            }

            Usuario usuario = usuarioRepository.findByEmailIgnoreCase(email)
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

            /*if (usuario.getEmailVerificado() == null || !usuario.getEmailVerificado()) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(Map.of("message", "Verifica tu correo antes de iniciar sesión."));
            }*/

            // ✅ Bloquear login SOLO si está explícitamente en FALSE.
            // (Si viene NULL por datos viejos/importados, no bloquear.)
            if (Boolean.FALSE.equals(usuario.getEmailVerificado())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(Map.of("message", "Verifica tu correo antes de iniciar sesión."));
            }

            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(usuario.getEmail(), password)
            );

            String token = jwtService.generateToken(usuario.getEmail());

            Map<String, Object> response = new HashMap<>();
            response.put("id", usuario.getId());
            response.put("nombre", usuario.getNombre());
            response.put("email", usuario.getEmail());
            response.put("token", token);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "Credenciales inválidas"));
        }
    }

    // =========================
    // REGISTER
    // =========================
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Usuario usuario) {
        try {
            if (usuario.getEmail() == null || usuario.getEmail().trim().isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("message", "El email es requerido"));
            }
            if (usuario.getNombre() == null || usuario.getNombre().trim().isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("message", "El nombre es requerido"));
            }
            if (usuario.getPassword() == null || usuario.getPassword().trim().isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("message", "La contraseña es requerida"));
            }

            String email = usuario.getEmail().trim();

            if (usuarioRepository.existsByEmailIgnoreCase(email)) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("message", "El email ya está registrado"));
            }

            List<Rol> roles = rolRepository.findByNombreContainingIgnoreCase("usuario");
            if (roles.isEmpty()) throw new RuntimeException("Rol usuario no encontrado");
            usuario.setRol(roles.get(0));

            usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));

            String token = UUID.randomUUID().toString().replace("-", "");
            usuario.setEmailVerificado(false);
            usuario.setVerificationToken(token);
            usuario.setVerificationTokenExpira(LocalDateTime.now().plusHours(verifyExpHours));
            usuario.setEmail(email);

            Usuario guardado = usuarioRepository.save(usuario);
            // ✅ Token de sesión para evitar login automático (que da 403 por no verificado)
            //String sessionToken = jwtService.generateToken(guardado.getEmail());


            String link = frontendBaseUrl + "/verify-email?token=" + token;

            String body =
                    "Hola " + guardado.getNombre() + " 👋\n\n" +
                    "Confirma tu correo para activar tu cuenta:\n" +
                    link + "\n\n" +
                    "Este enlace expira en " + verifyExpHours + " horas.\n";

            boolean emailEnviado = true;
            try {
                emailService.send(guardado.getEmail(), "Verifica tu correo - Sabor Oaxaca", body);
            } catch (Exception ex) {
                emailEnviado = false;
                System.err.println("⚠️ No se pudo enviar correo de verificación: " + ex.getMessage());
            }

            Map<String, Object> response = new HashMap<>();
            response.put("id", guardado.getId());
            response.put("nombre", guardado.getNombre());
            response.put("email", guardado.getEmail());
            response.put("emailEnviado", emailEnviado);
            response.put("message", "Cuenta creada. Revisa tu correo para verificar tu cuenta.");



            // ✅ NUEVO (mínimo y no rompe nada)
           // response.put("token", sessionToken);
            response.put("token", null);
            //response.put("usuario", guardado);
            Map<String, Object> usuarioSafe = new HashMap<>();
            usuarioSafe.put("id", guardado.getId());
            usuarioSafe.put("nombre", guardado.getNombre());
            usuarioSafe.put("email", guardado.getEmail());
            usuarioSafe.put("emailVerificado", guardado.getEmailVerificado());
            usuarioSafe.put("rol", guardado.getRol());

            response.put("usuario", usuarioSafe);




            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Error al crear cuenta: " + e.getMessage()));
        }
    }

    // =========================
    // VERIFY EMAIL
    // =========================
    @GetMapping("/verify-email")
    public ResponseEntity<?> verifyEmail(@RequestParam("token") String token) {
        try {
            if (token == null || token.trim().isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("message", "Token inválido"));
            }

            Usuario usuario = usuarioRepository.findByVerificationToken(token)
                    .orElseThrow(() -> new RuntimeException("Token inválido o ya fue usado"));

            if (usuario.getVerificationTokenExpira() != null &&
                usuario.getVerificationTokenExpira().isBefore(LocalDateTime.now())) {
                return ResponseEntity.status(HttpStatus.GONE)
                        .body(Map.of("message", "El token expiró. Solicita uno nuevo."));
            }

            usuario.setEmailVerificado(true);
            usuario.setVerificationToken(null);
            usuario.setVerificationTokenExpira(null);
            usuarioRepository.save(usuario);

            return ResponseEntity.ok(Map.of(
                    "message", "Correo verificado correctamente ✅",
                    "email", usuario.getEmail(),
                    "id", usuario.getId()
            ));

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", "Error verificando correo: " + e.getMessage()));
        }
    }

    // =========================
    // RESEND VERIFICATION
    // =========================
    @PostMapping("/resend-verification")
    public ResponseEntity<?> resendVerification(@RequestBody Map<String, String> body) {
        try {
            String email = body.get("email");
            if (email == null || email.trim().isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("message", "Email requerido"));
            }

            Usuario usuario = usuarioRepository.findByEmailIgnoreCase(email.trim())
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

            if (Boolean.TRUE.equals(usuario.getEmailVerificado())) {
                return ResponseEntity.ok(Map.of("message", "Este correo ya está verificado ✅"));
            }

            String token = UUID.randomUUID().toString().replace("-", "");
            usuario.setVerificationToken(token);
            usuario.setVerificationTokenExpira(LocalDateTime.now().plusHours(verifyExpHours));
            usuarioRepository.save(usuario);

            String link = frontendBaseUrl + "/verify-email?token=" + token;

            String mailBody =
                    "Hola " + usuario.getNombre() + " 👋\n\n" +
                    "Confirma tu correo para activar tu cuenta:\n" +
                    link + "\n\n" +
                    "Este enlace expira en " + verifyExpHours + " horas.\n";

            boolean emailEnviado = true;
            try {
                emailService.send(usuario.getEmail(), "Verifica tu correo - Sabor Oaxaca", mailBody);
            } catch (Exception ex) {
                emailEnviado = false;
                System.err.println("⚠️ No se pudo reenviar verificación: " + ex.getMessage());
            }

            return ResponseEntity.ok(Map.of(
                    "message", "Correo de verificación reenviado ✅",
                    "emailEnviado", emailEnviado
            ));

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Error reenviando verificación: " + e.getMessage()));
        }
    }

    // =========================
    // FORGOT PASSWORD
    // =========================
    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody Map<String, String> body) {
        String email = body.get("email");

        // ✅ Respuesta genérica SIEMPRE
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Si el correo existe, te enviamos un enlace para restablecer tu contraseña.");

        try {
            if (email == null || email.trim().isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("message", "Email requerido"));
            }

            Optional<Usuario> opt = usuarioRepository.findByEmailIgnoreCase(email.trim());
            if (opt.isEmpty()) return ResponseEntity.ok(response);

            Usuario usuario = opt.get();

            String token = UUID.randomUUID().toString().replace("-", "") + UUID.randomUUID().toString().replace("-", "");
            usuario.setResetPasswordToken(token);
            usuario.setResetPasswordTokenExpira(LocalDateTime.now().plusMinutes(resetExpMinutes));
            usuarioRepository.save(usuario);

            String link = frontendBaseUrl + "/reset-password?token=" + token;

            String mailBody =
                    "Hola " + usuario.getNombre() + " 👋\n\n" +
                    "Recibimos una solicitud para restablecer tu contraseña.\n\n" +
                    "Haz clic aquí para crear una nueva contraseña:\n" +
                    link + "\n\n" +
                    "Este enlace expira en " + resetExpMinutes + " minutos.\n\n" +
                    "Si tú no solicitaste esto, ignora este correo.";

            boolean emailEnviado = true;
            try {
                emailService.send(usuario.getEmail(), "Restablecer contraseña - Sabor Oaxaca", mailBody);
            } catch (Exception ex) {
                emailEnviado = false;
                System.err.println("⚠️ Falló envío correo reset: " + ex.getMessage());
            }

            response.put("emailEnviado", emailEnviado);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            // ✅ Nunca devolvemos 500 aquí para no romper el front
            e.printStackTrace();
            return ResponseEntity.ok(response);
        }
    }

    // =========================
    // RESET PASSWORD
    // =========================
    /*@PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody Map<String, String> body) {
        try {
            String token = body.get("token");
            String newPassword = body.get("password");

            if (token == null || token.trim().isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("message", "Token requerido"));
            }
            if (newPassword == null || newPassword.trim().length() < 6) {
                return ResponseEntity.badRequest().body(Map.of("message", "La contraseña debe tener al menos 6 caracteres"));
            }

            Usuario usuario = usuarioRepository.findByResetPasswordToken(token.trim())
                    .orElseThrow(() -> new RuntimeException("Token inválido o ya usado"));

            if (usuario.getResetPasswordTokenExpira() != null &&
                usuario.getResetPasswordTokenExpira().isBefore(LocalDateTime.now())) {
                return ResponseEntity.status(HttpStatus.GONE)
                        .body(Map.of("message", "El token expiró. Solicita uno nuevo."));
            }

            usuario.setPassword(passwordEncoder.encode(newPassword.trim()));
            usuario.setResetPasswordToken(null);
            usuario.setResetPasswordTokenExpira(null);
            usuarioRepository.save(usuario);

            return ResponseEntity.ok(Map.of("message", "Contraseña actualizada ✅"));

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", "Error restableciendo contraseña: " + e.getMessage()));
        }
    }*/

    /*@PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody Map<String, String> body) {
        try {
            String token = body.get("token");
            String newPassword = body.get("password");

            if (token == null || token.trim().isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("message", "Token requerido"));
            }
            if (newPassword == null || newPassword.trim().length() < 6) {
                return ResponseEntity.badRequest().body(Map.of("message", "La contraseña debe tener al menos 6 caracteres"));
            }

            Usuario usuario = usuarioRepository.findByResetPasswordToken(token.trim())
                    .orElseThrow(() -> new RuntimeException("Token inválido o ya usado"));

            if (usuario.getResetPasswordTokenExpira() != null &&
                usuario.getResetPasswordTokenExpira().isBefore(LocalDateTime.now())) {
                return ResponseEntity.status(HttpStatus.GONE)
                        .body(Map.of("message", "El token expiró. Solicita uno nuevo."));
            }

            usuario.setPassword(passwordEncoder.encode(newPassword.trim()));
            usuario.setResetPasswordToken(null);
            usuario.setResetPasswordTokenExpira(null);

            // ✅ CAMBIO MÍNIMO: permitir login aunque no se haya verificado por correo
            usuario.setEmailVerificado(true);
            usuario.setVerificationToken(null);
            usuario.setVerificationTokenExpira(null);

            usuarioRepository.save(usuario);

            return ResponseEntity.ok(Map.of("message", "Contraseña actualizada ✅"));

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", "Error restableciendo contraseña: " + e.getMessage()));
        }
    }*/


    // =========================
    // TERCER INTENTO
    // =========================
    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody Map<String, String> body) {
        try {
            String token = body.get("token");
            String newPassword = body.get("password");

            if (token == null || token.trim().isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("message", "Token requerido"));
            }
            if (newPassword == null || newPassword.trim().length() < 6) {
                return ResponseEntity.badRequest().body(Map.of("message", "La contraseña debe tener al menos 6 caracteres"));
            }

            Usuario usuario = usuarioRepository.findByResetPasswordToken(token.trim())
                    .orElseThrow(() -> new RuntimeException("Token inválido o ya usado"));

            if (usuario.getResetPasswordTokenExpira() != null &&
                usuario.getResetPasswordTokenExpira().isBefore(LocalDateTime.now())) {
                return ResponseEntity.status(HttpStatus.GONE)
                        .body(Map.of("message", "El token expiró. Solicita uno nuevo."));
            }

            usuario.setPassword(passwordEncoder.encode(newPassword.trim()));

            // ✅ CLAVE: si pudo resetear, verificamos el email
            usuario.setEmailVerificado(true);
            usuario.setVerificationToken(null);
            usuario.setVerificationTokenExpira(null);

            // limpia token reset
            usuario.setResetPasswordToken(null);
            usuario.setResetPasswordTokenExpira(null);

            usuarioRepository.save(usuario);

            return ResponseEntity.ok(Map.of("message", "Contraseña actualizada ✅"));

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", "Error restableciendo contraseña: " + e.getMessage()));
        }
    }
    

}

