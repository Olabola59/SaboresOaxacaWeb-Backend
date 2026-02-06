// ResenaService.java
package com.bluit.tourgatronomico.service;

import com.bluit.tourgatronomico.dto.ResenaRequest;
import com.bluit.tourgatronomico.model.Resena;
import com.bluit.tourgatronomico.repository.ResenaRepository;
import com.bluit.tourgatronomico.repository.UsuarioRepository;
import com.bluit.tourgatronomico.repository.PlatilloRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ResenaService {
    private final ResenaRepository resenaRepository;
    private final UsuarioRepository usuarioRepository;
    private final PlatilloRepository platilloRepository;

    @Autowired
    public ResenaService(ResenaRepository resenaRepository, 
                        UsuarioRepository usuarioRepository,
                        PlatilloRepository platilloRepository) {
        this.resenaRepository = resenaRepository;
        this.usuarioRepository = usuarioRepository;
        this.platilloRepository = platilloRepository;
    }

    public Resena crearResena(Resena resena) {
        resena.setFechaCreacion(LocalDateTime.now());
        return resenaRepository.save(resena);
    }

    public Resena crearResenaDesdeRequest(ResenaRequest request) {
        // Verificar que el usuario existe
        if (!usuarioRepository.existsById(request.getUsuarioId())) {
            throw new RuntimeException("Usuario no encontrado con ID: " + request.getUsuarioId());
        }
        
        // Verificar que el platillo existe
        if (!platilloRepository.existsById(request.getPlatilloId())) {
            throw new RuntimeException("Platillo no encontrado con ID: " + request.getPlatilloId());
        }
        
        // Verificar si ya existe una reseña de este usuario para este platillo
        /*if (resenaRepository.existsByUsuarioIdAndPlatilloId(request.getUsuarioId(), request.getPlatilloId())) {
            throw new RuntimeException("Ya has reseñado este platillo");
        }*/
        
        // Crear la reseña
        Resena resena = new Resena();
        resena.setUsuarioId(request.getUsuarioId());
        resena.setPlatilloId(request.getPlatilloId());
        resena.setCalificacion(request.getCalificacion().doubleValue()); // Convertir Integer a Double
        resena.setComentario(request.getComentario());
        resena.setFechaCreacion(LocalDateTime.now());
        
        return resenaRepository.save(resena);
    }

    public List<Resena> getResenasPorPlatillo(Long platilloId) {
        return resenaRepository.findByPlatilloId(platilloId);
    }

    public List<Resena> getResenasPorUsuario(Long usuarioId) {
        return resenaRepository.findByUsuarioId(usuarioId);
    }

    public boolean usuarioTieneResena(Long usuarioId, Long platilloId) {
        return resenaRepository.existsByUsuarioIdAndPlatilloId(usuarioId, platilloId);
    }

    public Resena getResenaPorId(Long id) {
        Optional<Resena> resena = resenaRepository.findById(id);
        return resena.orElse(null);
    }

    public Resena actualizarResena(Long id, Resena resenaActualizada) {
        Optional<Resena> resenaExistente = resenaRepository.findById(id);
        
        if (resenaExistente.isPresent()) {
            Resena resena = resenaExistente.get();
            resena.setCalificacion(resenaActualizada.getCalificacion());
            resena.setComentario(resenaActualizada.getComentario());
            resena.setFechaCreacion(LocalDateTime.now());
            
            return resenaRepository.save(resena);
        }
        
        return null;
    }

    public boolean eliminarResena(Long id) {
        if (resenaRepository.existsById(id)) {
            resenaRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public List<Resena> getAllResenas() {
        return resenaRepository.findAll();
    }

    public Double getCalificacionPromedioPorPlatillo(Long platilloId) {
        List<Resena> resenas = resenaRepository.findByPlatilloId(platilloId);
        
        return resenas.stream()
                .mapToDouble(Resena::getCalificacion)
                .average()
                .orElse(0.0);
    }

    public Long contarResenasPorPlatillo(Long platilloId) {
        return (long) resenaRepository.findByPlatilloId(platilloId).size();
    }
}
