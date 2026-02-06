package com.bluit.tourgatronomico.repository;

import com.bluit.tourgatronomico.model.Evento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface EventoRepository extends JpaRepository<Evento, Integer> {
    List<Evento> findByFechaAfter(LocalDateTime fecha);
    List<Evento> findByFechaBetween(LocalDateTime inicio, LocalDateTime fin);
}
