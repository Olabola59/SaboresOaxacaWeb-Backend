package com.bluit.tourgatronomico.repository;

import com.bluit.tourgatronomico.model.Favorito;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FavoritoRepository extends JpaRepository<Favorito, Long> {
    
    List<Favorito> findByUsuarioId(Long usuarioId);
    
    Optional<Favorito> findByUsuarioIdAndPlatilloId(Long usuarioId, Long platilloId);
    
    void deleteByUsuarioIdAndPlatilloId(Long usuarioId, Long platilloId);
    
    boolean existsByUsuarioIdAndPlatilloId(Long usuarioId, Long platilloId);
}