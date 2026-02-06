package com.bluit.tourgatronomico.controller;

import com.bluit.tourgatronomico.model.TipoLugar;
import com.bluit.tourgatronomico.service.TipoLugarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tipos-lugar")
@CrossOrigin(origins = "*")
public class TipoLugarController {

    @Autowired
    private TipoLugarService tipoLugarService;

    @GetMapping
    public List<TipoLugar> getAll() {
        return tipoLugarService.findAll();
    }

    @PostMapping
    public TipoLugar create(@RequestBody TipoLugar tipoLugar) {
        return tipoLugarService.save(tipoLugar);
    }
}