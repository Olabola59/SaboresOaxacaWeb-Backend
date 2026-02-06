package com.bluit.tourgatronomico.controller;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import com.bluit.tourgatronomico.model.Gusto;
import com.bluit.tourgatronomico.model.Pregunta;
import com.bluit.tourgatronomico.model.Respuesta;
import com.bluit.tourgatronomico.model.Usuario;
import com.bluit.tourgatronomico.repository.GustoRepository;
import com.bluit.tourgatronomico.repository.PreguntaRepository;
import com.bluit.tourgatronomico.repository.RespuestaRepository;
import com.bluit.tourgatronomico.repository.UsuarioRepository;

@RestController
@RequestMapping("/api/onboarding")
public class OnboardingController {

    @Autowired
    private PreguntaRepository preguntaRepository;

    @Autowired
    private RespuestaRepository respuestaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private GustoRepository gustoRepository;

    // 1️⃣ Obtener todas las preguntas con sus respuestas
    @GetMapping("/preguntas")
    public ResponseEntity<List<Pregunta>> getPreguntas() {
        try {
            List<Pregunta> preguntas = preguntaRepository.findAllWithRespuestas();
            return ResponseEntity.ok(preguntas);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }

    // 👇 Verificar si el usuario ya completó el onboarding
    @GetMapping("/gustos/{usuarioId}")
    public ResponseEntity<?> obtenerGustos(@PathVariable Long usuarioId) { // ✅ Long
        try {
            System.out.println("🔍 Verificando preferencias para usuario: " + usuarioId);
            
            // Verificar que el usuario existe
            Usuario usuario = usuarioRepository.findById(usuarioId)
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado: " + usuarioId));
            
            // Buscar los gustos del usuario
            List<Gusto> gustos = gustoRepository.findByUsuarioId(usuarioId);
            
            System.out.println("📦 Gustos encontrados: " + gustos.size());
            
            if (gustos.isEmpty()) {
                System.out.println("❌ Usuario no ha completado onboarding");
                return ResponseEntity.status(404).body(Map.of(
                    "completado", false,
                    "message", "Usuario no ha completado el onboarding"
                ));
            }
            
            // Formatear la respuesta con las preferencias
            Map<String, Object> response = new HashMap<>();
            response.put("completado", true);
            response.put("usuario_id", usuarioId);
            response.put("total_preferencias", gustos.size());
            
            // Opcional: Incluir los detalles de las preferencias
            List<Map<String, Object>> preferencias = gustos.stream().map(gusto -> {
                Map<String, Object> pref = new HashMap<>();
                pref.put("pregunta_id", gusto.getPreguntaId());
                pref.put("respuesta_id", gusto.getRespuestaId());
                
                // Opcional: Agregar los textos de pregunta y respuesta
                try {
                    Pregunta pregunta = preguntaRepository.findById(gusto.getPreguntaId()).orElse(null);
                    Respuesta respuesta = respuestaRepository.findById(gusto.getRespuestaId()).orElse(null);
                    
                    if (pregunta != null) {
                        pref.put("campo", pregunta.getCampo());
                    }
                    if (respuesta != null) {
                        pref.put("valor", respuesta.getValor());
                    }
                } catch (Exception e) {
                    System.err.println("Error obteniendo detalles: " + e.getMessage());
                }
                
                return pref;
            }).collect(Collectors.toList());
            
            response.put("preferencias", preferencias);
            
            System.out.println("✅ Usuario SÍ ha completado onboarding");
            return ResponseEntity.ok(response);
            
        } catch (RuntimeException e) {
            System.err.println("❌ Error: " + e.getMessage());
            return ResponseEntity.status(404).body(Map.of(
                "completado", false,
                "error", e.getMessage()
            ));
        } catch (Exception e) {
            System.err.println("❌ Error inesperado: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(500).body(Map.of(
                "completado", false,
                "error", "Error interno del servidor"
            ));
        }
    }

    // 2️⃣ Guardar preferencias del usuario
    @PostMapping("/gustos/{usuarioId}")
    @Transactional
    public ResponseEntity<?> guardarGustos(
            @PathVariable Long usuarioId, // ✅ Long
            @RequestBody Map<String, String> preferencias
    ) {
        try {
            System.out.println("=== Guardando preferencias ===");
            System.out.println("Usuario ID: " + usuarioId);
            System.out.println("Preferencias recibidas: " + preferencias);

            // Verificar que el usuario existe
            Usuario usuario = usuarioRepository.findById(usuarioId)
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado: " + usuarioId));

            System.out.println("Usuario encontrado: " + usuario.getNombre());

            // Eliminar gustos anteriores del usuario (por si quiere volver a hacer el onboarding)
            gustoRepository.deleteByUsuarioId(usuarioId);
            System.out.println("Gustos anteriores eliminados");

            // Guardar cada preferencia en la tabla gustos
            for (Map.Entry<String, String> entry : preferencias.entrySet()) {
                String campo = entry.getKey();
                String valor = entry.getValue();

                System.out.println("Procesando: " + campo + " = " + valor);

                Pregunta pregunta = preguntaRepository.findByCampo(campo)
                        .orElseThrow(() -> new RuntimeException("Pregunta no encontrada para campo: " + campo));

                Respuesta respuesta = respuestaRepository.findByPreguntaIdAndValor(pregunta.getId(), valor)
                        .orElseThrow(() -> new RuntimeException("Respuesta no encontrada para: " + valor));

                Gusto gusto = new Gusto();
                gusto.setUsuarioId(usuarioId);
                gusto.setPreguntaId(pregunta.getId());
                gusto.setRespuestaId(respuesta.getId());
                
                gustoRepository.save(gusto);
                
                System.out.println("✅ Gusto guardado: pregunta=" + pregunta.getId() + ", respuesta=" + respuesta.getId());
            }

            System.out.println("✅ Todas las preferencias guardadas exitosamente");

            return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Preferencias guardadas correctamente",
                "userId", usuarioId,
                "totalPreferencias", preferencias.size()
            ));
        } catch (Exception e) {
            System.err.println("❌ Error guardando preferencias: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(500).body(Map.of(
                "success", false,
                "error", e.getMessage()
            ));
        }
    }
}