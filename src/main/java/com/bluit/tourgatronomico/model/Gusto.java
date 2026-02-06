package com.bluit.tourgatronomico.model;

import jakarta.persistence.*;

@Entity
@Table(name = "gustos")
public class Gusto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "usuario_id")
    private Long usuarioId; // ✅ Long para coincidir con Usuario

    @Column(name = "pregunta_id")
    private Long preguntaId; // ✅ Long para coincidir con Pregunta

    @Column(name = "respuesta_id")
    private Long respuestaId; // ✅ Long para coincidir con Respuesta

    // Constructores
    public Gusto() {}

    public Gusto(Long usuarioId, Long preguntaId, Long respuestaId) {
        this.usuarioId = usuarioId;
        this.preguntaId = preguntaId;
        this.respuestaId = respuestaId;
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }

    public Long getPreguntaId() {
        return preguntaId;
    }

    public void setPreguntaId(Long preguntaId) {
        this.preguntaId = preguntaId;
    }

    public Long getRespuestaId() {
        return respuestaId;
    }

    public void setRespuestaId(Long respuestaId) {
        this.respuestaId = respuestaId;
    }
}