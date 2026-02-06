package com.bluit.tourgatronomico.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(
    name = "resena_likes",
    uniqueConstraints = @UniqueConstraint(columnNames = {"resena_id", "usuario_id"})
)
public class ResenaLike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "resena_id", nullable = false)
    private Long resenaId;

    @Column(name = "usuario_id", nullable = false)
    private Long usuarioId;

    @Column(name = "fecha")
    private LocalDateTime fecha = LocalDateTime.now();

    public ResenaLike() {}

    public ResenaLike(Long resenaId, Long usuarioId) {
        this.resenaId = resenaId;
        this.usuarioId = usuarioId;
        this.fecha = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public Long getResenaId() { return resenaId; }
    public Long getUsuarioId() { return usuarioId; }
    public LocalDateTime getFecha() { return fecha; }

    public void setId(Long id) { this.id = id; }
    public void setResenaId(Long resenaId) { this.resenaId = resenaId; }
    public void setUsuarioId(Long usuarioId) { this.usuarioId = usuarioId; }
    public void setFecha(LocalDateTime fecha) { this.fecha = fecha; }
}
