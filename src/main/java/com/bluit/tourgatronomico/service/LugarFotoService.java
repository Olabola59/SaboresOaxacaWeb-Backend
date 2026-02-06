package com.bluit.tourgatronomico.service;

import com.bluit.tourgatronomico.model.Lugar;
import com.bluit.tourgatronomico.model.LugarFoto;
import com.bluit.tourgatronomico.repository.LugarFotoRepository;
import com.bluit.tourgatronomico.repository.LugarRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class LugarFotoService {

    private static final String UPLOAD_DIR = "uploads/lugares/";

    @Autowired
    private LugarRepository lugarRepository;

    @Autowired
    private LugarFotoRepository lugarFotoRepository;

    @Transactional
    public LugarFoto guardarFoto(Long lugarId, MultipartFile archivo, String descripcion) throws Exception {

        Lugar lugar = lugarRepository.findById(lugarId)
                .orElseThrow(() -> new RuntimeException("Lugar no encontrado"));

        File dir = new File(UPLOAD_DIR);
        if (!dir.exists()) dir.mkdirs();

        String nombreArchivo = UUID.randomUUID() + "_" + archivo.getOriginalFilename();
        Path ruta = Paths.get(UPLOAD_DIR + nombreArchivo);
        Files.write(ruta, archivo.getBytes());

        LugarFoto foto = new LugarFoto();
        foto.setLugar(lugar);
        foto.setUrl("/uploads/lugares/" + nombreArchivo);
        foto.setDescripcion(descripcion);

        return lugarFotoRepository.save(foto);
    }
}
