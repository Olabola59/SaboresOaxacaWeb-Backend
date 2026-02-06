// ResenaController.java
package com.bluit.tourgatronomico.controller;

import com.bluit.tourgatronomico.dto.ResenaRequest;
import com.bluit.tourgatronomico.model.Resena;
import com.bluit.tourgatronomico.repository.ResenaRepository;
import com.bluit.tourgatronomico.service.ResenaService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/resenas")
public class ResenaController {
    private final ResenaService resenaService;
    private final ResenaRepository resenaRepository;

    @Autowired
    public ResenaController(ResenaService resenaService, ResenaRepository resenaRepository) {
        this.resenaService = resenaService;
        this.resenaRepository = resenaRepository;
    }

    @PostMapping
    public ResponseEntity<Resena> crearResena(@RequestBody ResenaRequest request) {
        try {
            Resena nuevaResena = resenaService.crearResenaDesdeRequest(request);
            return new ResponseEntity<>(nuevaResena, HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/platillo/{platilloId}")
    public ResponseEntity<List<Resena>> getResenasPorPlatillo(@PathVariable Long platilloId) {
        List<Resena> resenas = resenaService.getResenasPorPlatillo(platilloId);
        return new ResponseEntity<>(resenas, HttpStatus.OK);
    }

    @GetMapping("/usuario/{usuarioId}/platillo/{platilloId}/existe")
    public ResponseEntity<Boolean> usuarioTieneResena(
            @PathVariable Long usuarioId,
            @PathVariable Long platilloId) {
        boolean existe = resenaService.usuarioTieneResena(usuarioId, platilloId);
        return ResponseEntity.ok(existe);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Resena> getResenaPorId(@PathVariable Long id) {
        Resena resena = resenaService.getResenaPorId(id);
        if (resena != null) {
            return new ResponseEntity<>(resena, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<Resena>> getResenasPorUsuario(@PathVariable Long usuarioId) {
        List<Resena> resenas = resenaService.getResenasPorUsuario(usuarioId);
        return new ResponseEntity<>(resenas, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Resena> actualizarResena(
            @PathVariable Long id,
            @RequestBody Resena resena) {
        Resena resenaActualizada = resenaService.actualizarResena(id, resena);
        if (resenaActualizada != null) {
            return new ResponseEntity<>(resenaActualizada, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarResena(@PathVariable Long id) {
        boolean eliminada = resenaService.eliminarResena(id);
        if (eliminada) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping
    public ResponseEntity<List<Resena>> getAllResenas() {
        List<Resena> resenas = resenaService.getAllResenas();
        return new ResponseEntity<>(resenas, HttpStatus.OK);
    }

    @GetMapping("/platillo/{platilloId}/promedio")
    public ResponseEntity<Double> getCalificacionPromedio(@PathVariable Long platilloId) {
        Double promedio = resenaService.getCalificacionPromedioPorPlatillo(platilloId);
        return new ResponseEntity<>(promedio, HttpStatus.OK);
    }

    @GetMapping("/platillo/{platilloId}/cantidad")
    public ResponseEntity<Long> getCantidadResenas(@PathVariable Long platilloId) {
        Long cantidad = resenaService.contarResenasPorPlatillo(platilloId);
        return new ResponseEntity<>(cantidad, HttpStatus.OK);
    }

    // ===========================
    // ✅ NUEVO: Subir foto a una reseña
    // POST /api/resenas/{id}/foto  (multipart/form-data, campo: file)
    // ===========================
    @PostMapping("/{id}/foto")
    public ResponseEntity<?> uploadFotoResena(
            @PathVariable Long id,
            @RequestParam("file") MultipartFile file
    ) {
        try {
            if (file == null || file.isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("message", "Archivo vacío"));
            }

            Resena resena = resenaRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Reseña no encontrada con id " + id));

            String original = (file.getOriginalFilename() == null) ? "foto" : file.getOriginalFilename();
            String ext = "";
            int dot = original.lastIndexOf('.');
            if (dot >= 0) ext = original.substring(dot);

            String filename = "resena_" + id + "_" + UUID.randomUUID() + ext;

            Path uploadDir = Paths.get("uploads");
            if (!Files.exists(uploadDir)) {
                Files.createDirectories(uploadDir);
            }

            Path target = uploadDir.resolve(filename);
            Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);

            // Guarda ruta pública
            //resena.setFoto("/uploads/" + filename);
            String contentType = file.getContentType() == null ? "" : file.getContentType().toLowerCase();
            boolean isVideo = contentType.startsWith("video/");


            resena.setMediaTipo(isVideo ? "VIDEO" : "IMAGE");
            resena.setMediaUrl("/uploads/" + filename);

            Resena saved = resenaRepository.save(resena);

            return ResponseEntity.ok(saved);

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", e.getMessage()));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Error subiendo foto: " + e.getMessage()));
        }
    }

    @PostMapping("/{id}/media")
    public ResponseEntity<?> uploadMediaResena(
            @PathVariable Long id,
            @RequestParam("file") MultipartFile file
    ) {
        try {
            if (file == null || file.isEmpty()) {
                return ResponseEntity.badRequest().body(java.util.Map.of("message", "Archivo vacío"));
            }

            Resena resena = resenaRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Reseña no encontrada con id " + id));

            String contentType = file.getContentType() == null ? "" : file.getContentType().toLowerCase();
            boolean isImage = contentType.startsWith("image/");
            boolean isVideo = contentType.startsWith("video/");

            if (!isImage && !isVideo) {
                return ResponseEntity.badRequest().body(java.util.Map.of("message", "Solo se permite imagen o video"));
            }

            // ✅ límite simple (ajusta si quieres)
            long maxBytes = isVideo ? (25L * 1024 * 1024) : (10L * 1024 * 1024);
            if (file.getSize() > maxBytes) {
                return ResponseEntity.badRequest().body(java.util.Map.of(
                        "message", "Archivo demasiado grande. Máximo " + (isVideo ? "25MB" : "10MB")
                ));
            }

            String original = (file.getOriginalFilename() == null) ? "media" : file.getOriginalFilename();
            String ext = "";
            int dot = original.lastIndexOf('.');
            if (dot >= 0) ext = original.substring(dot);

            String filename = (isVideo ? "video_" : "img_") + "resena_" + id + "_" + java.util.UUID.randomUUID() + ext;

            java.nio.file.Path uploadDir = java.nio.file.Paths.get("uploads");
            if (!java.nio.file.Files.exists(uploadDir)) {
                java.nio.file.Files.createDirectories(uploadDir);
            }

            java.nio.file.Path target = uploadDir.resolve(filename);
            java.nio.file.Files.copy(file.getInputStream(), target, java.nio.file.StandardCopyOption.REPLACE_EXISTING);

            resena.setMediaTipo(isVideo ? "VIDEO" : "IMAGE");
            resena.setMediaUrl("/uploads/" + filename);

            Resena saved = resenaRepository.save(resena);
            return ResponseEntity.ok(saved);

        } catch (RuntimeException e) {
            return ResponseEntity.status(org.springframework.http.HttpStatus.NOT_FOUND)
                    .body(java.util.Map.of("message", e.getMessage()));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(java.util.Map.of("message", "Error subiendo media: " + e.getMessage()));
        }
    }

}
