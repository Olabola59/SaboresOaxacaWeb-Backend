package com.bluit.tourgatronomico.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "eventos")
public class Evento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String nombre;

    private String descripcion;

    private LocalDateTime fecha;

    @Column(name = "lugar_id")
    private Integer lugarId;

    private String foto;
}
