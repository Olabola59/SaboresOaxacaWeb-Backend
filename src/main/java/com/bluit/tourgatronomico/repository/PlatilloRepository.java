package com.bluit.tourgatronomico.repository;

import com.bluit.tourgatronomico.model.MomentoDia;
import com.bluit.tourgatronomico.model.Platillo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlatilloRepository extends JpaRepository<Platillo, Long> {

    // Buscar platillos por nombre (ignora mayúsculas/minúsculas)
    List<Platillo> findByNombreContainingIgnoreCase(String nombre);

    // Buscar todos los platillos de un lugar específico
    List<Platillo> findByLugarId(Long lugarId);
    List<Platillo> findByCategoriaIgnoreCase(String categoria);
    List<Platillo> findByMomentoDia(MomentoDia momentoDia);

}
