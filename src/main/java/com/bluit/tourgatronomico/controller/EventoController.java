package com.bluit.tourgatronomico.controller;

import com.bluit.tourgatronomico.model.Evento;
import com.bluit.tourgatronomico.service.EventoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/eventos")
public class EventoController {

    @Autowired
    private EventoService eventoService;

    // Obtener todos los eventos
    @GetMapping
    public List<Evento> obtenerEventos() {
        return eventoService.obtenerTodos();
    }

    // Obtener un evento por ID
    @GetMapping("/{id}")
    public ResponseEntity<Evento> obtenerEventoPorId(@PathVariable Integer id) {
        return eventoService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Crear un nuevo evento
    @PostMapping
    public ResponseEntity<Evento> crearEvento(@RequestBody Evento evento) {
        return ResponseEntity.ok(eventoService.guardar(evento));
    }

    // Actualizar un evento
    @PutMapping("/{id}")
    public ResponseEntity<Evento> actualizarEvento(
            @PathVariable Integer id,
            @RequestBody Evento evento) {

        return eventoService.obtenerPorId(id).map(e -> {
            evento.setId(id);
            return ResponseEntity.ok(eventoService.guardar(evento));
        }).orElse(ResponseEntity.notFound().build());
    }

    // Eliminar un evento
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarEvento(@PathVariable Integer id) {
        eventoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
