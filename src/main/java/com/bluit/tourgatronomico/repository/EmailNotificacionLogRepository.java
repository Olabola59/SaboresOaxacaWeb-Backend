package com.bluit.tourgatronomico.repository;

import com.bluit.tourgatronomico.model.EmailNotificacionLog;
import com.bluit.tourgatronomico.model.MomentoDia;
import com.bluit.tourgatronomico.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface EmailNotificacionLogRepository extends JpaRepository<EmailNotificacionLog, Long> {
  Optional<EmailNotificacionLog> findByUsuarioAndFechaAndMomentoDia(Usuario usuario, LocalDate fecha, MomentoDia momentoDia);
}
