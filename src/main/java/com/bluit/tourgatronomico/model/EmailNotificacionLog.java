package com.bluit.tourgatronomico.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(
  name = "email_notificacion_log",
  uniqueConstraints = @UniqueConstraint(
    name = "uk_email_log_usuario_fecha_momento",
    columnNames = {"usuario_id","fecha","momento_dia"}
  )
)
public class EmailNotificacionLog {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "usuario_id", nullable = false)
  private Usuario usuario;

  @Column(nullable = false)
  private LocalDate fecha;

  @Enumerated(EnumType.STRING)
  @Column(name = "momento_dia", nullable = false)
  private MomentoDia momentoDia;

  public EmailNotificacionLog() {}

  public EmailNotificacionLog(Usuario usuario, LocalDate fecha, MomentoDia momentoDia) {
    this.usuario = usuario;
    this.fecha = fecha;
    this.momentoDia = momentoDia;
  }

  public Long getId() { return id; }
  public Usuario getUsuario() { return usuario; }
  public LocalDate getFecha() { return fecha; }
  public MomentoDia getMomentoDia() { return momentoDia; }
}
