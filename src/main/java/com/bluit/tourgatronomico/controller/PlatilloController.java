/*package com.bluit.tourgatronomico.controller;

import com.bluit.tourgatronomico.model.Platillo;
import com.bluit.tourgatronomico.dto.PlatilloDTO;
import com.bluit.tourgatronomico.service.PlatilloService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

import java.util.List;

@RestController
@RequestMapping("/api/platillos")
public class PlatilloController {

    @Autowired
    private PlatilloService platilloService;

    @GetMapping
    public List<Platillo> getAll() {
        return platilloService.findAll();
    }

    @GetMapping("/{id}")
    public Platillo getById(@PathVariable Long id) {
        return platilloService.findById(id).orElse(null);
    }

    @PostMapping
    public Platillo create(@RequestBody Platillo platillo) {
        return platilloService.save(platillo);
    }

    @PutMapping("/{id}")
    public Platillo update(@PathVariable Long id, @RequestBody Platillo platillo) {
        platillo.setId(id);
        return platilloService.save(platillo);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        platilloService.delete(id);
    }

    @GetMapping("/recomendados/{userId}")
    public List<PlatilloDTO> getPlatillosRecomendados(@PathVariable Long userId) {
        return platilloService.getPlatillosRecomendadosParaUsuario(userId);
    }

    @GetMapping("/categoria/{categoria}")
    public List<PlatilloDTO> getPlatillosPorCategoria(@PathVariable String categoria) {
        return platilloService.getPlatillosPorCategoria(categoria);
    }

    @PostMapping(value = "/{id}/foto", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> subirFotoPlatillo(@PathVariable Long id, @RequestParam("foto") MultipartFile foto) {
        try {
            // 1) Validaciones básicas
            if (foto == null || foto.isEmpty()) {
                return ResponseEntity.badRequest().body("No se envió ninguna imagen.");
            }

            String contentType = foto.getContentType();
            if (contentType == null || !contentType.startsWith("image/")) {
                return ResponseEntity.badRequest().body("El archivo debe ser una imagen.");
            }

            // 2) Buscar platillo
            Platillo platillo = platilloService.findById(id)
                    .orElseThrow(() -> new RuntimeException("Platillo no encontrado"));

            // 3) Crear carpeta destino
            String uploadDir = "uploads/platillos";
            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)) Files.createDirectories(uploadPath);

            // 4) Nombre único
            String original = foto.getOriginalFilename() != null ? foto.getOriginalFilename() : "foto";
            String ext = "";
            int dot = original.lastIndexOf(".");
            if (dot >= 0) ext = original.substring(dot);

            String filename = UUID.randomUUID() + ext;

            // 5) Guardar archivo
            Path filePath = uploadPath.resolve(filename);
            Files.copy(foto.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            // 6) Guardar ruta pública (WebConfig ya expone /uploads/**)
            String publicUrl = "/uploads/platillos/" + filename;
            platillo.setFoto(publicUrl);
            platilloService.save(platillo);

            return ResponseEntity.ok(platillo);

        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error guardando la imagen: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error: " + e.getMessage());
        }
    }


}*/


package com.bluit.tourgatronomico.controller;

import com.bluit.tourgatronomico.model.Platillo;
import com.bluit.tourgatronomico.dto.PlatilloDTO;
import com.bluit.tourgatronomico.service.PlatilloService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/platillos")
public class PlatilloController {

    @Autowired
    private PlatilloService platilloService;

    @GetMapping
    public List<Platillo> getAll() {
        return platilloService.findAll();
    }

    @GetMapping("/{id}")
    public Platillo getById(@PathVariable Long id) {
        return platilloService.findById(id).orElse(null);
    }

    @PostMapping
    public Platillo create(@RequestBody Platillo platillo) {
        return platilloService.save(platillo);
    }

    @PutMapping("/{id}")
    public Platillo update(@PathVariable Long id, @RequestBody Platillo platillo) {
        platillo.setId(id);
        return platilloService.save(platillo);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        platilloService.delete(id);
    }

    @GetMapping("/recomendados/{userId}")
    public List<PlatilloDTO> getPlatillosRecomendados(@PathVariable Long userId) {
        return platilloService.getPlatillosRecomendadosParaUsuario(userId);
    }

    @GetMapping("/categoria/{categoria}")
    public List<PlatilloDTO> getPlatillosPorCategoria(@PathVariable String categoria) {
        return platilloService.getPlatillosPorCategoria(categoria);
    }

    // =========================================================
    // ✅ NUEVO: CREAR PLATILLO + FOTO EN LA MISMA PETICIÓN
    // POST /api/platillos/con-foto
    // multipart/form-data:
    //  - platillo: (application/json) { ... , lugar: { id: 1 } }
    //  - foto: (file) opcional
    // =========================================================
    @PostMapping(value = "/con-foto", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> crearPlatilloConFoto(
            @RequestPart("platillo") Platillo platillo,
            @RequestPart(value = "foto", required = false) MultipartFile foto
    ) {
        try {
            // 1) Guardar platillo primero (para obtener ID)
            Platillo saved = platilloService.save(platillo);

            // 2) Si viene foto, guardarla y actualizar campo foto
            if (foto != null && !foto.isEmpty()) {
                saved = guardarFotoEnPlatillo(saved, foto);
            }

            return ResponseEntity.ok(saved);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error creando platillo: " + e.getMessage());
        }
    }

    // =========================================================
    // ✅ EXISTENTE: Subir foto a un platillo ya creado
    // POST /api/platillos/{id}/foto (multipart/form-data, campo: foto)
    // =========================================================
    @PostMapping(value = "/{id}/foto", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> subirFotoPlatillo(@PathVariable Long id, @RequestParam("foto") MultipartFile foto) {
        try {
            if (foto == null || foto.isEmpty()) {
                return ResponseEntity.badRequest().body("No se envió ninguna imagen.");
            }

            Platillo platillo = platilloService.findById(id)
                    .orElseThrow(() -> new RuntimeException("Platillo no encontrado"));

            Platillo updated = guardarFotoEnPlatillo(platillo, foto);

            return ResponseEntity.ok(updated);

        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error guardando la imagen: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error: " + e.getMessage());
        }
    }

    // =========================================================
    // 🔧 Helper: guarda archivo en /uploads/platillos y setea platillo.foto = "/uploads/platillos/xxx"
    // =========================================================
    private Platillo guardarFotoEnPlatillo(Platillo platillo, MultipartFile foto) throws IOException {
        String contentType = foto.getContentType();
        if (contentType == null || !contentType.toLowerCase().startsWith("image/")) {
            throw new RuntimeException("El archivo debe ser una imagen.");
        }

        String uploadDir = "uploads/platillos";
        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) Files.createDirectories(uploadPath);

        String original = (foto.getOriginalFilename() != null) ? foto.getOriginalFilename() : "foto";
        String ext = "";
        int dot = original.lastIndexOf(".");
        if (dot >= 0) ext = original.substring(dot);

        String filename = UUID.randomUUID() + ext;

        Path filePath = uploadPath.resolve(filename);
        Files.copy(foto.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        String publicUrl = "/uploads/platillos/" + filename;
        platillo.setFoto(publicUrl);

        return platilloService.save(platillo);
    }
}
