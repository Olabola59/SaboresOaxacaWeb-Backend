package com.bluit.tourgatronomico.controller;

import com.bluit.tourgatronomico.model.Comentario;
import com.bluit.tourgatronomico.service.ComentarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comentarios")
public class ComentarioController {

    @Autowired
    private ComentarioService comentarioService;

    @GetMapping
    public List<Comentario> getAll() {
        return comentarioService.findAll();
    }

    @GetMapping("/{id}")
    public Comentario getById(@PathVariable Long id) {
        return comentarioService.findById(id).orElse(null);
    }

    @PostMapping
    public Comentario create(@RequestBody Comentario comentario) {
        return comentarioService.save(comentario);
    }

    @PutMapping("/{id}")
    public Comentario update(@PathVariable Long id, @RequestBody Comentario comentario) {
        comentario.setId(id);
        return comentarioService.save(comentario);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        comentarioService.delete(id);
    }
}
