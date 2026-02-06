package com.bluit.tourgatronomico.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "lugares_palabras_clave",
       uniqueConstraints = @UniqueConstraint(columnNames = {"lugar_id","palabra_clave_id"}))
public class LugarPalabraClave {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false) @JoinColumn(name = "lugar_id")
    private Lugar lugar;

    @ManyToOne(optional = false) @JoinColumn(name = "palabra_clave_id")
    private PalabraClave palabra;
}
