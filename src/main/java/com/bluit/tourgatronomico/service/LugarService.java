package com.bluit.tourgatronomico.service;

import com.bluit.tourgatronomico.dto.LugarCreateDTO;
import com.bluit.tourgatronomico.dto.PlatilloDTO;
import com.bluit.tourgatronomico.model.Lugar;
import com.bluit.tourgatronomico.model.Platillo;
import com.bluit.tourgatronomico.model.TipoLugar;
import com.bluit.tourgatronomico.repository.LugarRepository;
import com.bluit.tourgatronomico.repository.PlatilloRepository;
import com.bluit.tourgatronomico.repository.TipoLugarRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class LugarService {

    @Autowired
    private LugarRepository lugarRepository;

    @Autowired
    private PlatilloRepository platilloRepository;

    @Autowired
    private TipoLugarRepository tipoLugarRepository;

    @Autowired
    private ObjectMapper objectMapper;

    // ===== CRUD =====

    public List<Lugar> findAll() {
        return lugarRepository.findAll();
    }

    public Optional<Lugar> findById(Long id) {
        return lugarRepository.findById(id);
    }

    public Lugar save(Lugar lugar) {
        return lugarRepository.save(lugar);
    }

    public void delete(Long id) {
        lugarRepository.deleteById(id);
    }

    // ===== MÉTODO PARA FLUTTER =====

    @Transactional
    public Lugar guardarLugarDesdeFlutter(LugarCreateDTO dto) throws Exception {

        TipoLugar tipoLugar = tipoLugarRepository.findById(dto.getTipoLugarId())
                .orElseThrow(() -> new RuntimeException("Tipo de lugar no encontrado"));

        Lugar lugar = new Lugar();
        lugar.setNombre(dto.getNombre());
        lugar.setDescripcion(dto.getDescripcion());
        lugar.setDireccion(dto.getDireccion());
        lugar.setLatitud(dto.getLatitud());
        lugar.setLongitud(dto.getLongitud());
        lugar.setTelefono(dto.getTelefono());
        lugar.setUsuarioId(dto.getUsuarioId());
        lugar.setTipoLugar(tipoLugar);
        lugar.setFechaRegistro(LocalDateTime.now());

        // Guardar horarios como JSON
        lugar.setHorario(objectMapper.writeValueAsString(dto.getHorario()));

        Lugar guardado = lugarRepository.save(lugar);

        if (dto.getPlatillos() != null) {
            for (PlatilloDTO p : dto.getPlatillos()) {
                Platillo platillo = new Platillo();
                platillo.setNombre(p.getNombre());
                platillo.setDescripcion(p.getDescripcion());
                platillo.setPrecio(p.getPrecio());
                platillo.setHistoria(p.getHistoria());
                platillo.setFoto(p.getFoto());
                platillo.setCategoria(p.getCategoria());
                platillo.setIngredientes(
                    p.getIngredientes() == null ? null : p.getIngredientes().toArray(new String[0])
                );
                platillo.setLugar(guardado);

                platilloRepository.save(platillo);
            }
        }

        return guardado;
    }
}
