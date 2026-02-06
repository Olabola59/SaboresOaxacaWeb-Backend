// ResenaRepository.java
package com.bluit.tourgatronomico.repository;

import com.bluit.tourgatronomico.model.Resena;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository


public interface ResenaRepository extends JpaRepository<Resena, Long> {
    List<Resena> findByPlatilloId(Long platilloId);
    List<Resena> findByUsuarioId(Long usuarioId);
    boolean existsByUsuarioIdAndPlatilloId(Long usuarioId, Long platilloId);

    long countByUsuarioId(Long usuarioId); // ✅ nuevo
}