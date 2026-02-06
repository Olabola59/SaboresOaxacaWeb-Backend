package com.bluit.tourgatronomico.service;

import com.bluit.tourgatronomico.model.*;
import com.bluit.tourgatronomico.repository.NotificacionRepository;
import com.bluit.tourgatronomico.repository.UsuarioPalabraClaveRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class NotificacionMatchEmailService {

  private final UsuarioPalabraClaveRepository usuarioPalRepo;
  private final NotificacionRepository notificacionRepo;
  private final EmailService emailService;

  @Value("${app.frontend.base-url:http://localhost:5173}")
  private String frontendBaseUrl;

  public NotificacionMatchEmailService(
      UsuarioPalabraClaveRepository usuarioPalRepo,
      NotificacionRepository notificacionRepo,
      EmailService emailService
  ) {
    this.usuarioPalRepo = usuarioPalRepo;
    this.notificacionRepo = notificacionRepo;
    this.emailService = emailService;
  }

  public void notificarNuevoLugarPorPalabra(Lugar lugar, PalabraClave palabra) {
    List<UsuarioPalabraClave> matches = usuarioPalRepo.findByPalabraId(palabra.getId());

    // Evitar duplicados si por alguna razón viene repetido
    Set<Long> usuariosNotificados = new HashSet<>();

    for (UsuarioPalabraClave up : matches) {
      Usuario u = up.getUsuario();
      if (u == null || u.getId() == null) continue;
      if (!Boolean.TRUE.equals(u.getEmailVerificado())) continue;
      if (!usuariosNotificados.add(u.getId())) continue;

      String titulo = "Nuevo restaurante que coincide con tus gustos 🍽️";
      String mensaje = "Se agregó \"" + lugar.getNombre() + "\" (coincide con: " + palabra.getNombre() + ").";

      // Notificación in-app
      Notificacion n = new Notificacion();
      n.setUsuario(u);
      n.setTitulo(titulo);
      n.setMensaje(mensaje);
      n.setFechaEnvio(LocalDateTime.now());
      n.setLeido(false);
      notificacionRepo.save(n);

      // Correo
      String link = frontendBaseUrl + "/establecimientos/" + lugar.getId(); // ajusta si tu ruta es distinta
      String subject = "Sabor Oaxaca - " + titulo;
      String body =
          "Hola " + u.getNombre() + " 👋\n\n" +
          "¡Hay un nuevo restaurante!\n\n" +
          "📍 " + lugar.getNombre() + "\n" +
          "✨ Coincide con tu gusto: " + palabra.getNombre() + "\n\n" +
          "Ver detalles: " + link + "\n\n" +
          "— Sabor Oaxaca";

      emailService.send(u.getEmail(), subject, body);
    }
  }

  public void notificarNuevoPlatilloPorPalabra(Platillo platillo, PalabraClave palabra) {
    List<UsuarioPalabraClave> matches = usuarioPalRepo.findByPalabraId(palabra.getId());

    Set<Long> usuariosNotificados = new HashSet<>();

    for (UsuarioPalabraClave up : matches) {
      Usuario u = up.getUsuario();
      if (u == null || u.getId() == null) continue;
      if (!Boolean.TRUE.equals(u.getEmailVerificado())) continue;
      if (!usuariosNotificados.add(u.getId())) continue;

      String titulo = "Nuevo platillo que coincide con tus gustos 😋";
      String lugar = (platillo.getLugar() != null) ? platillo.getLugar().getNombre() : "un establecimiento";
      String mensaje = "Se agregó \"" + platillo.getNombre() + "\" en " + lugar + " (coincide con: " + palabra.getNombre() + ").";

      Notificacion n = new Notificacion();
      n.setUsuario(u);
      n.setTitulo(titulo);
      n.setMensaje(mensaje);
      n.setFechaEnvio(LocalDateTime.now());
      n.setLeido(false);
      notificacionRepo.save(n);

      String link = frontendBaseUrl + "/platillos/" + platillo.getId(); // ajusta si tu ruta es distinta
      String subject = "Sabor Oaxaca - " + titulo;
      String body =
          "Hola " + u.getNombre() + " 👋\n\n" +
          "¡Se agregó un nuevo platillo!\n\n" +
          "🍲 " + platillo.getNombre() + "\n" +
          "🏪 En: " + lugar + "\n" +
          "✨ Coincide con tu gusto: " + palabra.getNombre() + "\n\n" +
          "Ver detalles: " + link + "\n\n" +
          "— Sabor Oaxaca";

      emailService.send(u.getEmail(), subject, body);
    }
  }
}
