package com.bluit.tourgatronomico.controller;

import com.bluit.tourgatronomico.model.Recomendacion;
import com.bluit.tourgatronomico.service.RecomendacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recomendaciones")
public class RecomendacionController {

    
    @Autowired
    private RecomendacionService recomendacionService;

    @GetMapping
    public List<Recomendacion> getAll() {
        return recomendacionService.findAll();
    }

    @GetMapping("/{id}")
    public Recomendacion getById(@PathVariable Long id) {
        return recomendacionService.findById(id).orElse(null);
    }

    @PostMapping
    public Recomendacion create(@RequestBody Recomendacion recomendacion) {
        return recomendacionService.save(recomendacion);
    }

    @PutMapping("/{id}")
    public Recomendacion update(@PathVariable Long id, @RequestBody Recomendacion recomendacion) {
        recomendacion.setId(id);
        return recomendacionService.save(recomendacion);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        recomendacionService.delete(id);
    }
}
