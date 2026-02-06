package com.bluit.tourgatronomico.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "notificacion_dispositivo")
public class NotificacionDispositivo {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false) @JoinColumn(name = "notificacion_id")
    private Notificacion notificacion;

    @ManyToOne(optional = false) @JoinColumn(name = "dispositivo_id")
    private Dispositivo dispositivo;

    @Column(name = "estado_envio", length = 20)
    private String estadoEnvio; // PENDING / SENT / FAILED
}
