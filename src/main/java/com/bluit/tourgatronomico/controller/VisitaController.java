// ======================= CONTROLLER =======================
package com.bluit.tourgatronomico.controller;

import com.bluit.tourgatronomico.model.Visita;
import com.bluit.tourgatronomico.service.VisitaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/visitas")
public class VisitaController {

    @Autowired
    private VisitaService visitaService;

    @GetMapping
    public List<Visita> getAll() {
        return visitaService.findAll();
    }

    @GetMapping("/{id}")
    public Visita getById(@PathVariable Long id) {
        return visitaService.findById(id).orElse(null);
    }

    @PostMapping
    public Visita create(@RequestBody Visita visita) {
        return visitaService.save(visita);
    }

    @PutMapping("/{id}")
    public Visita update(@PathVariable Long id, @RequestBody Visita visita) {
        visita.setId(id);
        return visitaService.save(visita);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        visitaService.delete(id);
    }
}
