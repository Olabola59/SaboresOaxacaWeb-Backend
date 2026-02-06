package com.bluit.tourgatronomico.repository;

import com.bluit.tourgatronomico.model.Lugar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LugarRepository extends JpaRepository<Lugar, Long> {

    // Buscar lugares por nombre (ignora mayúsculas/minúsculas)
    List<Lugar> findByNombreContainingIgnoreCase(String nombre);

    // Buscar lugares por usuario dueño
    List<Lugar> findByUsuarioId(Long usuarioId);

    // Buscar lugares por tipo
    List<Lugar> findByTipoLugarId(Long tipoLugarId);
}
