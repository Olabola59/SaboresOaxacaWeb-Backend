package com.bluit.tourgatronomico.repository;

import com.bluit.tourgatronomico.model.Pregunta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PreguntaRepository extends JpaRepository<Pregunta, Long> {
    
    @Query("SELECT p FROM Pregunta p LEFT JOIN FETCH p.respuestas")
    List<Pregunta> findAllWithRespuestas();
    
    // ✅ Este método debe devolver Optional<Pregunta>
    Optional<Pregunta> findByCampo(String campo);
}