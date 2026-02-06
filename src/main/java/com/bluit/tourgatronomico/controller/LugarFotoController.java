package com.bluit.tourgatronomico.controller;

import com.bluit.tourgatronomico.model.LugarFoto;
import com.bluit.tourgatronomico.service.LugarFotoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/lugares")
public class LugarFotoController {

    @Autowired
    private LugarFotoService lugarFotoService;

    @PostMapping("/{id}/fotos")
    public LugarFoto subirFoto(
            @PathVariable Long id,
            @RequestParam("foto") MultipartFile foto,
            @RequestParam(value = "descripcion", required = false) String descripcion
    ) throws Exception {

        return lugarFotoService.guardarFoto(id, foto, descripcion);
    }
}
