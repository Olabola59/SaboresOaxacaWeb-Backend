// GustoRepository
package com.bluit.tourgatronomico.repository;

import com.bluit.tourgatronomico.model.Gusto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface GustoRepository extends JpaRepository<Gusto, Long> { // ✅ Long
    List<Gusto> findByUsuarioId(Long usuarioId);
    void deleteByUsuarioId(Long usuarioId);
}