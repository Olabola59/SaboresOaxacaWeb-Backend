package com.bluit.tourgatronomico.service;

import com.bluit.tourgatronomico.model.*;
import com.bluit.tourgatronomico.repository.EmailNotificacionLogRepository;
import com.bluit.tourgatronomico.repository.PlatilloRepository;
import com.bluit.tourgatronomico.repository.UsuarioRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Random;

@Service
public class EmailNotificacionScheduler {

  private final UsuarioRepository usuarioRepository;
  private final PlatilloRepository platilloRepository;
  private final EmailNotificacionLogRepository logRepository;
  private final EmailService emailService;

  private final Random random = new Random();

  public EmailNotificacionScheduler(
      UsuarioRepository usuarioRepository,
      PlatilloRepository platilloRepository,
      EmailNotificacionLogRepository logRepository,
      EmailService emailService
  ) {
    this.usuarioRepository = usuarioRepository;
    this.platilloRepository = platilloRepository;
    this.logRepository = logRepository;
    this.emailService = emailService;
  }

  // 8:00 a.m.
  @Scheduled(cron = "0 0 8 * * *", zone = "America/Mexico_City")
  public void desayuno() { enviar(MomentoDia.DESAYUNO); }

  // 12:01 p.m.
  @Scheduled(cron = "0 1 12 * * *", zone = "America/Mexico_City")
  public void comida() { enviar(MomentoDia.COMIDA); }

  // 18:01 p.m.
  @Scheduled(cron = "0 1 18 * * *", zone = "America/Mexico_City")
  public void cena() { enviar(MomentoDia.CENA); }

  private void enviar(MomentoDia momento) {
    LocalDate hoy = LocalDate.now();

    List<Usuario> usuarios = usuarioRepository.findAllByEmailVerificadoTrue();
    if (usuarios.isEmpty()) return;

    // Si agregaste campo momentoDia en Platillo:
    List<Platillo> candidatos = platilloRepository.findByMomentoDia(momento);

    // Fallback si no hay candidatos:
    if (candidatos == null || candidatos.isEmpty()) {
      candidatos = platilloRepository.findAll();
    }
    if (candidatos.isEmpty()) return;

    for (Usuario u : usuarios) {
      boolean yaEnviado = logRepository.findByUsuarioAndFechaAndMomentoDia(u, hoy, momento).isPresent();
      if (yaEnviado) continue;

      Platillo p = candidatos.get(random.nextInt(candidatos.size()));
      String lugar = (p.getLugar() != null && p.getLugar().getNombre() != null) ? p.getLugar().getNombre() : "un establecimiento";

      String subject = "Sabor Oaxaca - Recomendación de " + momento.name().toLowerCase();
      String body =
          "Hola " + u.getNombre() + " 👋\n\n" +
          "Hoy hay " + p.getNombre() + " en " + lugar + " 🍽️\n\n" +
          "¡Entra a Sabor Oaxaca para ver más detalles!";

      emailService.send(u.getEmail(), subject, body);
      logRepository.save(new EmailNotificacionLog(u, hoy, momento));
    }
  }
}
