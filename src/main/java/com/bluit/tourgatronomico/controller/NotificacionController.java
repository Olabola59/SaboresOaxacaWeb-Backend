package com.bluit.tourgatronomico.controller;

import com.bluit.tourgatronomico.model.Dispositivo;
import com.bluit.tourgatronomico.model.Notificacion;
import com.bluit.tourgatronomico.service.NotificacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/notificaciones")
public class NotificacionController {

    @Autowired
    private NotificacionService service;

    // ---- Dispositivos ----
    @PostMapping("/dispositivos/registrar")
    public Dispositivo registrar(@RequestBody Map<String, Object> body) {
        Integer usuarioId = body.get("usuarioId") == null ? null : (Integer) body.get("usuarioId");
        String token = (String) body.get("token");
        String plataforma = (String) body.getOrDefault("plataforma", "ANDROID");
        return service.registrarToken(usuarioId, token, plataforma);
    }

    @GetMapping("/dispositivos/usuario/{usuarioId}")
    public Optional<Dispositivo> dispositivos(@PathVariable Integer usuarioId) {
        return service.dispositivosDeUsuario(usuarioId);
    }

    @DeleteMapping("/dispositivos/{id}")
    public ResponseEntity<Void> desactivar(@PathVariable Long id) {
        service.desactivar(id);
        return ResponseEntity.noContent().build();
    }

    // ---- Envío ----
    @PostMapping("/usuarios/{usuarioId}/enviar")
    public Notificacion enviarUsuario(@PathVariable Integer usuarioId, @RequestBody Map<String, String> body) {
        return service.enviarAUsuario(usuarioId, body.get("titulo"), body.get("mensaje"), body.get("dataJson"));
    }

    @PostMapping("/broadcast")
    public Notificacion broadcast(@RequestBody Map<String, String> body) {
        return service.enviarBroadcast(body.get("titulo"), body.get("mensaje"), body.get("dataJson"));
    }

    // ---- Lectura ----
    @GetMapping("/usuario/{usuarioId}")
    public List<Notificacion> listarUsuario(@PathVariable Long usuarioId) {
        return service.listarPorUsuario(usuarioId);
    }

    @PutMapping("/{id}/leer")
    public Notificacion marcarLeida(@PathVariable Long id) {
        return service.marcarComoLeida(id);
    }

    @PutMapping("/usuario/{usuarioId}/leer-todas")
    public ResponseEntity<Void> leerTodas(@PathVariable Long usuarioId) {
        service.marcarTodasComoLeidas(usuarioId);
        return ResponseEntity.noContent().build();
    }


}
