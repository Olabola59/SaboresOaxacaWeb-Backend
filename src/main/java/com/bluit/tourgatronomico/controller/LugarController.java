package com.bluit.tourgatronomico.controller;

import com.bluit.tourgatronomico.dto.LugarCreateDTO;
import com.bluit.tourgatronomico.model.Lugar;
import com.bluit.tourgatronomico.service.LugarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/lugares")
public class LugarController {

    @Autowired
    private LugarService lugarService;

    // ===== CRUD EXISTENTE =====

    @GetMapping
    public List<Lugar> getAll() {
        return lugarService.findAll();
    }

    @GetMapping("/{id}")
    public Lugar getById(@PathVariable Long id) {
        return lugarService.findById(id).orElse(null);
    }

    @PostMapping
    public Lugar create(@RequestBody Lugar lugar) {
        return lugarService.save(lugar);
    }

    @PutMapping("/{id}")
    public Lugar update(@PathVariable Long id, @RequestBody Lugar lugar) {
        lugar.setId(id);
        return lugarService.save(lugar);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        lugarService.delete(id);
    }

    // ===== ENDPOINT PARA FLUTTER =====

    @PostMapping("/crear-completo")
    public Lugar crearDesdeFlutter(@RequestBody LugarCreateDTO dto) throws Exception {
        return lugarService.guardarLugarDesdeFlutter(dto);
    }
}
