package com.bluit.tourgatronomico.controller;

import com.bluit.tourgatronomico.model.LugarPalabraClave;
import com.bluit.tourgatronomico.model.UsuarioPalabraClave;
import com.bluit.tourgatronomico.service.PreferenciaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.bluit.tourgatronomico.model.PlatilloPalabraClave;


import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/preferencias")
public class PreferenciaController {

    @Autowired
    private PreferenciaService service;

    // --- Usuario ---
    @GetMapping("/usuarios/{usuarioId}")
    public List<UsuarioPalabraClave> listarUsuario(@PathVariable Long usuarioId) {
        return service.listarPorUsuario(usuarioId);
    }

    @PostMapping("/usuarios/{usuarioId}/add")
    public ResponseEntity<?> agregarUsuario(@PathVariable Long usuarioId, @RequestBody Map<String, Long> body) {
        service.agregarAUsuario(usuarioId, body.get("palabraId"));
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/usuarios/{usuarioId}/remove/{palabraId}")
    public ResponseEntity<?> quitarUsuario(@PathVariable Long usuarioId, @PathVariable Long palabraId) {
        service.quitarDeUsuario(usuarioId, palabraId);
        return ResponseEntity.noContent().build();
    }

    // --- Lugar
    @GetMapping("/lugares/{lugarId}")
    public List<LugarPalabraClave> listarLugar(@PathVariable Long lugarId) {
        return service.listarPorLugar(lugarId);
    }

    @PostMapping("/lugares/{lugarId}/add")
    public ResponseEntity<?> agregarLugar(@PathVariable Long lugarId, @RequestBody Map<String, Long> body) {
        service.agregarALugar(lugarId, body.get("palabraId"));
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/lugares/{lugarId}/remove/{palabraId}")
    public ResponseEntity<?> quitarLugar(@PathVariable Long lugarId, @PathVariable Long palabraId) {
        service.quitarDeLugar(lugarId, palabraId);
        return ResponseEntity.noContent().build();
    }

    // --- Platillo ---
    @GetMapping("/platillos/{platilloId}")
    public List<PlatilloPalabraClave> listarPlatillo(@PathVariable Long platilloId) {
        return service.listarPorPlatillo(platilloId);
    }

    @PostMapping("/platillos/{platilloId}/add")
    public ResponseEntity<?> agregarPlatillo(@PathVariable Long platilloId, @RequestBody Map<String, Long> body) {
        service.agregarAPlatillo(platilloId, body.get("palabraId"));
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/platillos/{platilloId}/remove/{palabraId}")
    public ResponseEntity<?> quitarPlatillo(@PathVariable Long platilloId, @PathVariable Long palabraId) {
        service.quitarDePlatillo(platilloId, palabraId);
        return ResponseEntity.noContent().build();
    }

}
