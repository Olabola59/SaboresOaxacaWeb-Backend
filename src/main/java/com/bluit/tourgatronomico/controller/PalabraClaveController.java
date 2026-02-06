package com.bluit.tourgatronomico.controller;

import com.bluit.tourgatronomico.model.PalabraClave;
import com.bluit.tourgatronomico.service.PalabraClaveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/palabras-clave")
public class PalabraClaveController {

    @Autowired
    private PalabraClaveService service;

    @GetMapping
    public List<PalabraClave> getAll() {
        return service.findAll();
    }

    @GetMapping("/buscar")
    public List<PalabraClave> search(@RequestParam String q) {
        return service.search(q);
    }

    @PostMapping
    public PalabraClave create(@RequestBody PalabraClave p) {
        return service.save(p);
    }

    @PutMapping("/{id}")
    public PalabraClave update(@PathVariable Long id, @RequestBody PalabraClave p) {
        p.setId(id);
        return service.save(p);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
