package com.bluit.tourgatronomico.controller;

import com.bluit.tourgatronomico.model.Favorito;
import com.bluit.tourgatronomico.model.Platillo;
import com.bluit.tourgatronomico.model.Usuario;
import com.bluit.tourgatronomico.service.FavoritoService;
import com.bluit.tourgatronomico.repository.PlatilloRepository;
import com.bluit.tourgatronomico.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/favoritos")
public class FavoritoController {

    private final FavoritoService favoritoService;
    private final UsuarioRepository usuarioRepository;
    private final PlatilloRepository platilloRepository;

    @Autowired
    public FavoritoController(FavoritoService favoritoService, 
                             UsuarioRepository usuarioRepository,
                             PlatilloRepository platilloRepository) {
        this.favoritoService = favoritoService;
        this.usuarioRepository = usuarioRepository;
        this.platilloRepository = platilloRepository;
    }

    // Agregar a favoritos
    @PostMapping
    public ResponseEntity<?> agregarFavorito(@RequestBody Map<String, Object> request) {
        try {
            Long usuarioId = Long.valueOf(request.get("usuarioId").toString());
            Long platilloId = Long.valueOf(request.get("platilloId").toString());

            System.out.println("=== AGREGAR FAVORITO ===");
            System.out.println("Usuario ID: " + usuarioId);
            System.out.println("Platillo ID: " + platilloId);

            // Verificar si ya existe
            if (favoritoService.existsByUsuarioIdAndPlatilloId(usuarioId, platilloId)) {
                Map<String, String> response = new HashMap<>();
                response.put("message", "El platillo ya está en favoritos");
                return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
            }

            // Obtener usuario y platillo
            Usuario usuario = usuarioRepository.findById(usuarioId)
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

            Platillo platillo = platilloRepository.findById(platilloId)
                    .orElseThrow(() -> new RuntimeException("Platillo no encontrado"));

            // Crear favorito
            Favorito favorito = new Favorito(usuario, platillo);
            Favorito favoritoGuardado = favoritoService.save(favorito);

            System.out.println("✅ Favorito agregado con ID: " + favoritoGuardado.getId());

            Map<String, Object> response = new HashMap<>();
            response.put("id", favoritoGuardado.getId());
            response.put("message", "Agregado a favoritos");

            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (Exception e) {
            System.err.println("❌ Error al agregar favorito: " + e.getMessage());
            e.printStackTrace();

            Map<String, String> error = new HashMap<>();
            error.put("message", "Error al agregar favorito: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    // Eliminar de favoritos
    @DeleteMapping("/{usuarioId}/{platilloId}")
    @Transactional
    public ResponseEntity<?> eliminarFavorito(
            @PathVariable Long usuarioId,
            @PathVariable Long platilloId) {
        try {
            System.out.println("=== ELIMINAR FAVORITO ===");
            System.out.println("Usuario ID: " + usuarioId);
            System.out.println("Platillo ID: " + platilloId);

            // Verificar si existe
            if (!favoritoService.existsByUsuarioIdAndPlatilloId(usuarioId, platilloId)) {
                Map<String, String> response = new HashMap<>();
                response.put("message", "El platillo no está en favoritos");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }

            // Eliminar
            favoritoService.deleteByUsuarioIdAndPlatilloId(usuarioId, platilloId);

            System.out.println("✅ Favorito eliminado");

            Map<String, String> response = new HashMap<>();
            response.put("message", "Eliminado de favoritos");

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            System.err.println("❌ Error al eliminar favorito: " + e.getMessage());
            e.printStackTrace();

            Map<String, String> error = new HashMap<>();
            error.put("message", "Error al eliminar favorito: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    // Obtener IDs de favoritos de un usuario
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<?> obtenerFavoritosUsuario(@PathVariable Long usuarioId) {
        try {
            System.out.println("=== OBTENER FAVORITOS ===");
            System.out.println("Usuario ID: " + usuarioId);

            List<Favorito> favoritos = favoritoService.findByUsuarioId(usuarioId);

            List<Map<String, Object>> response = favoritos.stream()
                    .map(fav -> {
                        Map<String, Object> map = new HashMap<>();
                        map.put("id", fav.getId());
                        map.put("platilloId", fav.getPlatillo().getId());
                        map.put("fechaAgregado", fav.getFecha());
                        return map;
                    })
                    .collect(Collectors.toList());

            System.out.println("✅ Favoritos encontrados: " + response.size());

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            System.err.println("❌ Error al obtener favoritos: " + e.getMessage());
            e.printStackTrace();

            Map<String, String> error = new HashMap<>();
            error.put("message", "Error al obtener favoritos: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    // Verificar si un platillo es favorito
    @GetMapping("/usuario/{usuarioId}/platillo/{platilloId}")
    public ResponseEntity<?> verificarFavorito(
            @PathVariable Long usuarioId,
            @PathVariable Long platilloId) {
        try {
            System.out.println("=== VERIFICAR FAVORITO ===");
            System.out.println("Usuario ID: " + usuarioId);
            System.out.println("Platillo ID: " + platilloId);

            boolean esFavorito = favoritoService.existsByUsuarioIdAndPlatilloId(usuarioId, platilloId);

            Map<String, Boolean> response = new HashMap<>();
            response.put("esFavorito", esFavorito);

            System.out.println("✅ Es favorito: " + esFavorito);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            System.err.println("❌ Error al verificar favorito: " + e.getMessage());
            e.printStackTrace();

            Map<String, String> error = new HashMap<>();
            error.put("message", "Error al verificar favorito: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    // Obtener platillos favoritos completos de un usuario
    @GetMapping("/usuario/{usuarioId}/platillos")
    public ResponseEntity<?> obtenerPlatillosFavoritos(@PathVariable Long usuarioId) {
        try {
            System.out.println("=== OBTENER PLATILLOS FAVORITOS ===");
            System.out.println("Usuario ID: " + usuarioId);

            List<Favorito> favoritos = favoritoService.findByUsuarioId(usuarioId);

            List<Platillo> platillos = favoritos.stream()
                    .map(Favorito::getPlatillo)
                    .collect(Collectors.toList());

            System.out.println("✅ Platillos favoritos encontrados: " + platillos.size());

            return ResponseEntity.ok(platillos);

        } catch (Exception e) {
            System.err.println("❌ Error al obtener platillos favoritos: " + e.getMessage());
            e.printStackTrace();

            Map<String, String> error = new HashMap<>();
            error.put("message", "Error al obtener platillos favoritos: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
}