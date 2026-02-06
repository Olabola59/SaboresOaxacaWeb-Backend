package com.bluit.tourgatronomico.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "dispositivos")
public class Dispositivo {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne @JoinColumn(name = "usuario_id")
    private Usuario usuario; // opcional

    @Column(name = "token_fcm", nullable = false, unique = true, length = 300)
    private String tokenFcm;

    @Column(length = 20)
    private String plataforma; // ANDROID / IOS / WEB

    @Column(name = "fecha_registro")
    private LocalDateTime fechaRegistro;

    private Boolean activo;

    @PrePersist
    void prePersist() {
        if (fechaRegistro == null) fechaRegistro = LocalDateTime.now();
        if (activo == null) activo = true;
    }
}
