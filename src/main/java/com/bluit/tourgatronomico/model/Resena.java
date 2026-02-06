package com.bluit.tourgatronomico.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "resenas")
public class Resena {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "usuario_id", nullable = false)
    private Long usuarioId;

    @Column(name = "platillo_id", nullable = false)
    private Long platilloId;

    @Column(nullable = false)
    private Double calificacion;

    @Column(length = 1000)
    private String comentario;

    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion;

    // ✅ coincide con tu BD
    @Column(name = "media_tipo", length = 255)
    private String mediaTipo; // "IMAGE" | "VIDEO" | null

    @Column(name = "media_url", length = 255)
    private String mediaUrl;  // "/uploads/archivo.ext" | null

    public Resena() {}

    public Resena(Long usuarioId, Long platilloId, Double calificacion, String comentario) {
        this.usuarioId = usuarioId;
        this.platilloId = platilloId;
        this.calificacion = calificacion;
        this.comentario = comentario;
        this.fechaCreacion = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getUsuarioId() { return usuarioId; }
    public void setUsuarioId(Long usuarioId) { this.usuarioId = usuarioId; }

    public Long getPlatilloId() { return platilloId; }
    public void setPlatilloId(Long platilloId) { this.platilloId = platilloId; }

    public Double getCalificacion() { return calificacion; }
    public void setCalificacion(Double calificacion) { this.calificacion = calificacion; }

    public String getComentario() { return comentario; }
    public void setComentario(String comentario) { this.comentario = comentario; }

    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }

    public String getMediaTipo() { return mediaTipo; }
    public void setMediaTipo(String mediaTipo) { this.mediaTipo = mediaTipo; }

    public String getMediaUrl() { return mediaUrl; }
    public void setMediaUrl(String mediaUrl) { this.mediaUrl = mediaUrl; }
}
