package com.bluit.tourgatronomico.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "notificaciones")
public class Notificacion {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne @JoinColumn(name = "usuario_id")
    private Usuario usuario;            // puede ser null para broadcast

    @Column(nullable = false)
    private String titulo;

    @Column(nullable = false, length = 1000)
    private String mensaje;

    @Column(name = "fecha_envio")
    private LocalDateTime fechaEnvio;

    private Boolean leido;

    @PrePersist
    void prePersist() {
        if (fechaEnvio == null) fechaEnvio = LocalDateTime.now();
        if (leido == null) leido = false;
    }
}
