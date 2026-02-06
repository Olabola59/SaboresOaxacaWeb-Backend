package com.bluit.tourgatronomico.repository;

import com.bluit.tourgatronomico.model.Respuesta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RespuestaRepository extends JpaRepository<Respuesta, Long> {
    Optional<Respuesta> findByPreguntaIdAndValor(Long preguntaId, String valor);
}